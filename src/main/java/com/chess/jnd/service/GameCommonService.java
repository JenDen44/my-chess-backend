package com.chess.jnd.service;

import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.Figure;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.chess.jnd.error_handling.GameNotFoundException;
import com.chess.jnd.error_handling.GameWrongDataException;
import com.chess.jnd.notification.GameNotificationService;
import com.chess.jnd.utils.GameMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameCommonService {

    private final GameRedisService gameRedisService;
    private final GameService gameService;
    private final GameInfoService gameInfoService;
    private final JwtService jwtService;
    private final GameMapper gameMapper;
    private final ObjectMapper mapper;
    private final GameNotificationService notificationService;

    @Autowired
    public GameCommonService(GameRedisService gameRedisService, GameService gameService, GameInfoService gameInfoService, JwtService jwtService, GameMapper gameMapper, ObjectMapper mapper, GameNotificationService notificationService) {
        this.gameRedisService = gameRedisService;
        this.gameService = gameService;
        this.gameInfoService = gameInfoService;
        this.jwtService = jwtService;
        this.gameMapper = gameMapper;
        this.mapper = mapper;
        this.notificationService = notificationService;
    }

    public GameRedis saveGame(GameRedis game) throws JsonProcessingException {
        gameService.save(gameMapper.mapToGame(game));

        return gameRedisService.save(game);
    }

    public GameRedis findGameById(Integer gameId) throws JsonProcessingException {
        GameRedis game = gameRedisService.get(gameId);

        if (game == null) {
            Game gameFromDB = gameService.get(gameId);
            game = gameRedisService.save(gameMapper.mapToGameRedis(gameFromDB));
        }

        return game;
    }

    public GameRedis findGameByToken(String token) throws JsonProcessingException {
        return findGameById(jwtService.getGameId(token));
    }

    public void deleteGame(Integer gameId) {
        gameRedisService.delete(gameId);
        gameService.delete(gameId);
    }

    public ShortFigureName[][] initBoard() {
        ShortFigureName shortNames [] [] = {
                {ShortFigureName.BLACK_ROOK, ShortFigureName.BLACK_KNIGHT, ShortFigureName.BLACK_BISHOP, ShortFigureName.BLACK_QUEEN, ShortFigureName.BLACK_KING, ShortFigureName.BLACK_BISHOP, ShortFigureName.BLACK_KNIGHT, ShortFigureName.BLACK_ROOK },
                {ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                { ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN },
                { ShortFigureName.WHITE_ROOK, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_KING, ShortFigureName.WHITE_KING, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_ROOK },
        };
        return shortNames;
    }

    public GameResponse createGame(CreateGameRequest gameRequest) throws JsonProcessingException {
        ShortFigureName[][] shortFigureNames = initBoard();
        String board  = mapper.writeValueAsString(shortFigureNames);
        Game game = new Game(board);

        Game savedGameToDB = gameService.save(game);
        String whiteToken = jwtService.generateCustomToken(savedGameToDB.getId(), Color.WHITE);
        String blackToken = jwtService.generateCustomToken(savedGameToDB.getId(), Color.BLACK);

        savedGameToDB.setTokenForWhitePlayer(whiteToken);
        savedGameToDB.setTokenForBlackPlayer(blackToken);

        savedGameToDB = gameService.save(game);
        GameRedis gameRedis = gameRedisService.save(gameMapper.mapToGameRedis(savedGameToDB));

        return GameResponse.builder()
                .info(gameRedis.getGameInfo())
                .tokenForBlackPlayer(gameRedis.getTokenForBlackPlayer())
                .tokenForWhitePlayer(gameRedis.getTokenForWhitePlayer())
                .board(gameRedis.getBoard().getShortNames())
                .active(gameRedis.getActive())
                .currentColor(gameRequest.getColor())
                .build();
    }

    public GameResponse getCurrentGame(HttpServletRequest request) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameByToken(token);

        return GameResponse.builder()
                .info(game.getGameInfo())
                .tokenForBlackPlayer(game.getTokenForBlackPlayer())
                .tokenForWhitePlayer(game.getTokenForWhitePlayer())
                .board(game.getBoard().getShortNames())
                .active(game.getActive())
                .currentColor(jwtService.getColor(token))
                .build();
    }

    public String getOppositeToken(GameRedis game, String currentToken) {
        return game.getTokenForBlackPlayer().equals(currentToken) ? game.getTokenForWhitePlayer() : game.getTokenForBlackPlayer();
    }

    public GameInfoResponse move(HttpServletRequest request, MoveRequest moveRequest) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameByToken(token);

        if (game.getGameInfo().getStatus().equals(GameStatus.FINISHED)) {
            throw new GameWrongDataException("The game is already finished");
        }

        Board board = game.getBoard();
        Cell cellFrom = board.getCell(moveRequest.getFromX(), moveRequest.getFromY());
        Cell cellTo = board.getCell(moveRequest.getToX(), moveRequest.getToY());
        Figure figureFrom = cellFrom.getFigure();

        if (figureFrom == null) {
            throw new GameNotFoundException("Figure doesn't exist in cell from");
        }

        if (!figureFrom.getColor().equals(game.getActive())) {
            throw new GameWrongDataException("The cell from color should be " + game.getActive().getColor());
        }

        if (!figureFrom.canMove(cellTo)) {
            throw new GameWrongDataException("The figure " + figureFrom + " can't move to cell " + cellTo);
        }

        figureFrom.move(cellTo);
        game.setActive(game.getActive() == Color.WHITE ? Color.BLACK : Color.WHITE);
        // TODO GameInfo change state (save to GameRedis and Game work?)
        saveGame(game);

        GameInfo gameInfo = game.getGameInfo();
        String oppositeToken = getOppositeToken(game, token);

        MoveNotify moveNotify = MoveNotify.builder()
                .activeColor(game.getActive())
                .fromY(moveRequest.getFromY())
                .fromX(moveRequest.getFromX())
                .toX(moveRequest.getToX())
                .toY(moveRequest.getToY())
                .build();

        notificationService.sendNotificationForMove(moveNotify, oppositeToken);

        return GameInfoResponse.builder()
                .detail(gameInfo.getDetail())
                .status(gameInfo.getStatus())
                .build();
    }

    public GameInfoResponse giveUp(HttpServletRequest request) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameByToken(token);
        String oppositeToken = getOppositeToken(game, token);
        GameInfo gameInfo = game.getGameInfo();

        if (gameInfo.getStatus().equals(GameStatus.FINISHED)) {
            throw new GameWrongDataException("The game is already finished");
        }

        gameInfo.setStatus(GameStatus.FINISHED);
        gameInfo.setDetail(jwtService.getColor(token).equals(Color.BLACK) ? GameResult.WHITE : GameResult.BLACK);
        saveGame(game);

        GameStatusNotify gameStatusNotify =  GameStatusNotify.builder()
                .detail(gameInfo.getDetail())
                .status(gameInfo.getStatus())
                .build();

        notificationService.sendNotificationForGameStatus(gameStatusNotify, oppositeToken);

        return GameInfoResponse.builder()
                .detail(gameInfo.getDetail())
                .status(gameInfo.getStatus())
                .build();
    }
}
