package com.chess.jnd.service;

import com.chess.jnd.client.ChatClient;
import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.Figure;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.chess.jnd.error_handling.GameNotFoundException;
import com.chess.jnd.error_handling.GameWrongDataException;
import com.chess.jnd.notification.GameNotificationService;
import com.chess.jnd.mappers.GameInfoGameInfoResponseMapper;
import com.chess.jnd.mappers.GameToRedisGameMapperImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@Slf4j
public class GameCommonService {

    private final GameRedisService gameRedisService;
    private final GameService gameService;
    private final GameInfoService gameInfoService;
    private final JwtService jwtService;
    private final GameToRedisGameMapperImpl gameMapper;
    private final ObjectMapper mapper;
    private final GameNotificationService notificationService;
    private final GameInfoGameInfoResponseMapper gameInfoMapper;
    private TimerTask task = null;
    private final ChatClient chatClient;

    @Value("${time.for.move}")
    public Integer timeForeMove;

    @Autowired
    public GameCommonService(GameRedisService gameRedisService, GameService gameService, GameInfoService gameInfoService, JwtService jwtService, GameToRedisGameMapperImpl gameMapper, ObjectMapper mapper, GameNotificationService notificationService, GameInfoGameInfoResponseMapper gameInfoMapper, ChatClient chatClient) {
        this.gameRedisService = gameRedisService;
        this.gameService = gameService;
        this.gameInfoService = gameInfoService;
        this.jwtService = jwtService;
        this.gameMapper = gameMapper;
        this.mapper = mapper;
        this.notificationService = notificationService;
        this.gameInfoMapper = gameInfoMapper;
        this.chatClient = chatClient;
    }

    public GameRedis saveGame(GameRedis game) {
        gameService.save(gameMapper.gameRedisToGame(game));

        return gameRedisService.save(game);
    }

    public GameRedis findGameById(Integer gameId) {
        GameRedis game = gameRedisService.get(gameId);

        log.debug("game from redis {}", game);

        if (game == null) {
            Game gameFromDB = gameService.get(gameId);

            log.debug("game from db {}", gameFromDB);

            game = gameRedisService.save(gameMapper.gameToGameRedis(gameFromDB));
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
                { ShortFigureName.WHITE_ROOK, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_QUEEN, ShortFigureName.WHITE_KING, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_ROOK },
        };
        return shortNames;
    }

    public GameResponse createGame(CreateGameRequest gameRequest) throws JsonProcessingException {

        if (gameRequest.getTimeForMove() != null) {
            timeForeMove = gameRequest.getTimeForMove();
        }

        ShortFigureName[][] shortFigureNames = initBoard();
        String board  = mapper.writeValueAsString(shortFigureNames);

        log.info("newly created board {}", board);

        Game game = new Game(board);

        Game savedGameToDB = gameService.save(game);
        String whiteToken = jwtService.generateCustomToken(savedGameToDB.getId(), Color.WHITE);
        String blackToken = jwtService.generateCustomToken(savedGameToDB.getId(), Color.BLACK);

        ChatResponse chatResponse = chatClient.createChat(List.of(whiteToken, blackToken));

        if (chatResponse == null) {
            throw new GameWrongDataException("Chat was not created");
        }

        savedGameToDB.setTokenForWhitePlayer(whiteToken);
        savedGameToDB.setTokenForBlackPlayer(blackToken);
        savedGameToDB.setTimeForMove(timeForeMove);

        savedGameToDB = gameService.save(game);
        GameRedis gameRedis = gameRedisService.save(gameMapper.gameToGameRedis(savedGameToDB));

        finishGameForTimer(whiteToken, true);

        log.debug("newly created game mapped to redis {}", gameRedis );

        return GameResponse.builder()
                .info(gameInfoMapper.gameInfoToGameInfoResponse(gameRedis.getGameInfo()))
                .tokenForBlackPlayer(gameRedis.getTokenForBlackPlayer())
                .tokenForWhitePlayer(gameRedis.getTokenForWhitePlayer())
                .board(gameRedis.getBoard())
                .active(gameRedis.getActive())
                .timeForMove(timeForeMove)
                .currentColor(gameRequest.getColor())
                .date(gameRedis.getDate())
                .build();
    }

    public GameResponse getCurrentGame(HttpServletRequest request) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameByToken(token);

        log.debug("current game by token {}", game);

        return GameResponse.builder()
                .info(gameInfoMapper.gameInfoToGameInfoResponse(game.getGameInfo()))
                .tokenForBlackPlayer(game.getTokenForBlackPlayer())
                .tokenForWhitePlayer(game.getTokenForWhitePlayer())
                .board(game.getBoard())
                .active(game.getActive())
                .currentColor(jwtService.getColor(token))
                .date(game.getDate())
                .build();
    }

    public String getOppositeToken(GameRedis game, String currentToken) {
        return game.getTokenForBlackPlayer().equals(currentToken) ? game.getTokenForWhitePlayer() : game.getTokenForBlackPlayer();
    }

    public GameInfoResponse move(HttpServletRequest request, MoveRequest moveRequest) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameByToken(token);

        log.info("one player requested move for game {}", game);

        if (game.getGameInfo().getStatus().equals(GameStatus.FINISHED)) {
            log.error("game is finished the move can't be performed");
            throw new GameWrongDataException("The game is already finished");
        }

        Board board = new Board(game.getBoard(), game.getPassantCell(), game.getPrevStep());
        Cell cellFrom = board.getCell(moveRequest.getFromX(), moveRequest.getFromY());
        Cell cellTo = board.getCell(moveRequest.getToX(), moveRequest.getToY());

        log.debug("cell to {}, cell from {} ", cellTo, cellFrom);

        Figure figureFrom = cellFrom.getFigure();
        GameInfo gameInfo = game.getGameInfo();

        if (figureFrom == null) {
            log.error("Figure doesn't exist in cell from");
            throw new GameNotFoundException("Figure doesn't exist in cell from");
        }

        if (!figureFrom.getColor().equals(game.getActive())) {
            log.error("he cell from color should be {}", game.getActive().getColor());
            throw new GameWrongDataException("The cell from color should be " + game.getActive().getColor());
        }

        if (!figureFrom.canMove(cellTo)) {
            log.error("The figure " + figureFrom + " can't move to cell " + cellTo);
            throw new GameWrongDataException("The figure " + figureFrom + " can't move to cell " + cellTo);
        }

        figureFrom = cellFrom.getFigure();

        figureFrom.move(cellTo,true);
        game.setBoard(board.getShortNames());
        Cell passantCell = board.getPassantCell();

        log.debug("passantCell {}", passantCell);

        if (passantCell == null) game.setPassantCell(null);
            else game.setPassantCell(new PassantCell(passantCell.getX(), passantCell.getY()));

        game.setPrevStep(board.getPrevStep());

        game.setActive(game.getActive() == Color.WHITE ? Color.BLACK : Color.WHITE);

        String oppositeToken = getOppositeToken(game, token);

        boolean isCheckMate = board.isCheckMate(game.getActive());
        boolean isDraw = board.isDraw(game.getActive());

        if (isCheckMate) {
            GameResult result = game.getActive() == Color.WHITE ? GameResult.BLACK : GameResult.WHITE;

            log.debug("Game is finished won {}", gameInfo.getDetail());

            changeGameStatusAndNotifyPlayers(gameInfo, result, GameStatus.FINISHED, oppositeToken, token);

        } else if (isDraw) {
            log.debug("Game is finished status {}", gameInfo.getDetail());

            changeGameStatusAndNotifyPlayers(gameInfo, GameResult.DRAW, GameStatus.FINISHED, oppositeToken, token);
        }
        game.setDate(LocalDateTime.now());
        saveGame(game);

        MoveNotify moveNotify = MoveNotify.builder()
                .activeColor(game.getActive())
                .fromY(moveRequest.getFromY())
                .fromX(moveRequest.getFromX())
                .toX(moveRequest.getToX())
                .toY(moveRequest.getToY())
                .date(game.getDate())
                .build();

        log.debug("notification for move {}", moveNotify);

        notificationService.sendNotificationForMove(moveNotify, oppositeToken);

        finishGameForTimer(oppositeToken, !isCheckMate && !isDraw);

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

        log.debug("one player gave up in the game {}", game);

        if (gameInfo.getStatus().equals(GameStatus.FINISHED)) {
            log.error("The game is already finished");
            throw new GameWrongDataException("The game is already finished");
        }

        GameResult result = jwtService.getColor(token).equals(Color.BLACK) ? GameResult.WHITE : GameResult.BLACK;

        changeGameStatusAndNotifyPlayers(gameInfo, result, GameStatus.FINISHED, oppositeToken, token);

        saveGame(game);
        return GameInfoResponse.builder()
                .detail(gameInfo.getDetail())
                .status(gameInfo.getStatus())
                .build();
    }

    public void changeGameStatusAndNotifyPlayers(GameInfo gameInfo, GameResult result, GameStatus status, String... tokens) throws JsonProcessingException {
        gameInfo.setDetail(result);
        gameInfo.setStatus(status);

        if (status.equals(GameStatus.FINISHED)) {
           GameRedis curGame = findGameByToken(tokens[0]);
           curGame.setFinishDate(LocalDateTime.now());
           saveGame(curGame);
        }

        gameInfoService.save(gameInfo);

        GameStatusNotify gameStatusNotify =  GameStatusNotify.builder()
                .detail(gameInfo.getDetail())
                .status(gameInfo.getStatus())
                .build();

        log.debug("notification for status {}", gameStatusNotify);

        notificationService.sendNotificationForGameStatus(gameStatusNotify, tokens);


    }

    public void offerDraw(HttpServletRequest request) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameByToken(token);

        if (game.getGameInfo().getStatus().equals(GameStatus.FINISHED)) {
            log.error("The game is already finished");
            throw new GameWrongDataException("The game is already finished");
        }

        String oppositeToken = getOppositeToken(game, token);

        log.debug("one player with color {} offered draw", game.getActive());

        notificationService.sendNotificationForDraw(oppositeToken);
    }

    public void draw(HttpServletRequest request, boolean answer) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameByToken(token);

        String oppositeToken = getOppositeToken(game, token);
        GameInfo gameInfo = game.getGameInfo();

        if (gameInfo.getStatus().equals(GameStatus.FINISHED)) {
            log.error("The game is already finished");
            throw new GameWrongDataException("The game is already finished");
        }

        log.debug("player with color {} is agreed for draw ? {}", game.getActive(), answer);

        notificationService.sendNotificationForDrawResponse(answer, oppositeToken);

        if (answer) {
            changeGameStatusAndNotifyPlayers(gameInfo, GameResult.DRAW, GameStatus.FINISHED, token, oppositeToken);
            saveGame(game);
        }
    }

    public void finishGame(String token) throws JsonProcessingException {
        GameRedis game = findGameByToken(token);
        String oppositeToken = getOppositeToken(game, token);
        GameInfo gameInfo = game.getGameInfo();
        GameResult result = jwtService.getColor(token).equals(Color.BLACK) ? GameResult.WHITE : GameResult.BLACK;

        if (gameInfo.getStatus().equals(GameStatus.FINISHED)) return;

        changeGameStatusAndNotifyPlayers(game.getGameInfo(), result, GameStatus.FINISHED, token, oppositeToken);
        saveGame(game);
    }

    public void finishGameForTimer(String token, boolean newTimer) throws JsonProcessingException {
        if (task != null) task.cancel();
        GameRedis game = findGameByToken(token);

        if (newTimer) {
            Timer timer = new Timer();
            task = new TimerTask() {
                public void run() {
                    try {
                    finishGame(token);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            timer.schedule(task, game.getTimeForMove());
        }
    }
}
