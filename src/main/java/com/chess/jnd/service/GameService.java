package com.chess.jnd.service;

import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.chess.jnd.repository.GameRepository;
import com.chess.jnd.utils.GameMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final GameInfoService gameInfoService;
    private final ObjectMapper mapper;
    private final GameMapper gameMapper;
    private final JwtService jwtService;
    private final GameRedisService redisService;

    @Autowired
    public GameService(GameRepository gameRepository, GameInfoService gameInfoService, ObjectMapper mapper, GameMapper gameMapper, JwtService jwtService, GameRedisService redisService) {
        this.gameRepository = gameRepository;
        this.gameInfoService = gameInfoService;
        this.mapper = mapper;
        this.gameMapper = gameMapper;
        this.jwtService = jwtService;
        this.redisService = redisService;
    }

    public GameResponse createNewGame(CreateGameRequest gameRequest) throws JsonProcessingException {
        ShortFigureName[][] shortFigureNames = initBoard();
        String board  = mapper.writeValueAsString(shortFigureNames);
        Game game = new Game(board);
        gameInfoService.saveGameInfo(game.getInfoId());
        GameRedis savedGameToDB = saveGame(game);

        String whiteToken = jwtService.generateCustomToken(savedGameToDB.getId(),Color.WHITE);
        String blackToken = jwtService.generateCustomToken(savedGameToDB.getId(), Color.BLACK);
        savedGameToDB.setTokenForWhitePlayer(whiteToken);
        savedGameToDB.setTokenForBlackPlayer(blackToken);
        savedGameToDB = saveGame(gameMapper.mapToGame(savedGameToDB));

        return GameResponse.builder()
                .info(savedGameToDB.getInfoId())
                .tokenForBlackPlayer(savedGameToDB.getTokenForBlackPlayer())
                .tokenForWhitePlayer(savedGameToDB.getTokenForWhitePlayer())
                .board(savedGameToDB.getBoard().getShortNames())
                .active(savedGameToDB.getActive())
                .currentColor(gameRequest.getColor())
                .build();
    }

    public GameRedis findGameById(Integer id) throws JsonProcessingException {
        var game = redisService.get(id);

        if (game == null) {
             Game gameFromDB = gameRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Game with id " + id + " is not found in DB"));

          game = redisService.save(gameMapper.mapToGameRedis(gameFromDB));
        }

        return game;
    }

    public GameRedis findGameByToken(String token) throws JsonProcessingException {
        return findGameById(jwtService.getGameId(token));
    }

    public GameRedis saveGame(Game game) throws JsonProcessingException {
        redisService.save(gameMapper.mapToGameRedis(game));

        return gameMapper.mapToGameRedis(gameRepository.save(game));
    }

    public GameRedis updateGame(Game game, Integer id) throws JsonProcessingException {
        var gameFromDb = findGameById(id);

        gameFromDb = gameMapper.mapToGameRedis(game);
        saveGame(gameMapper.mapToGame(gameFromDb));

        return gameFromDb;
    }

    public void deleteGame(Integer id) throws JsonProcessingException {
        findGameById(id);

        redisService.delete(id);
        gameRepository.deleteById(id);
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

    public GameResponse getCurrentGame(HttpServletRequest request) throws JsonProcessingException {
        String token = jwtService.resolveToken(request);
        GameRedis game = findGameById(jwtService.getGameId(token));

        return GameResponse.builder()
                .info(game.getInfoId())
                .tokenForBlackPlayer(game.getTokenForBlackPlayer())
                .tokenForWhitePlayer(game.getTokenForWhitePlayer())
                .board(game.getBoard().getShortNames())
                .active(game.getActive())
                .currentColor(jwtService.getColor(token))
                .build();
    }

    public void move(HttpServletRequest request, MoveRequest moveRequest) {
        //fromX FromY -> board.getCell
        //ToY ToX -> board.getCell
        // get Figure fromCell ( only from) if null or color is not Active throw error
        //figure.canMove -> false (throw error)
        //figure.move
        // save new board version and change active color to oposite
        //calculate gameInfo (return Game Info Object in response)
        //socket for second player from and to coordinates and new active color and current game info
    }

    public void giveUp(HttpServletRequest request) {

    }
}