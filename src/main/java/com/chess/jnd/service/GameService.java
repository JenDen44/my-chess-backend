package com.chess.jnd.service;

import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.chess.jnd.repository.GameRedisRepository;
import com.chess.jnd.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final GameInfoService gameInfoService;
    private final ObjectMapper mapper;
    private final GameRedisRepository redisRepository;

    @Autowired
    public GameService(GameRepository gameRepository, GameInfoService gameInfoService, ObjectMapper mapper, GameRedisRepository redisRepository) {
        this.gameRepository = gameRepository;
        this.gameInfoService = gameInfoService;
        this.mapper = mapper;
        this.redisRepository = redisRepository;
    }

    public GameResponse createNewGame(CreateGameRequest gameRequest) throws JsonProcessingException {
        ShortFigureName[][] shortFigureNames = initBoard();
        String board  = mapper.writeValueAsString(shortFigureNames);
        Game game = new Game(board);
        gameInfoService.saveGameInfo(game.getInfoId());
        Game savedGameToDB = saveGame(game);
        saveGameToRedisById(savedGameToDB);
        saveGameToRedisByToken(savedGameToDB);

        return GameResponse.builder()
                .info(savedGameToDB.getInfoId())
                .tokenForBlackPlayer(savedGameToDB.getTokenForBlackPlayer())
                .tokenForWhitePlayer(savedGameToDB.getTokenForWhitePlayer())
                .board(mapper.readValue(savedGameToDB.getBoard(), ShortFigureName[][].class))
                .active(savedGameToDB.getActive())
                .currentColor(gameRequest.getColor())
                .build();
    }

    public Game findGameById(Integer id) throws JsonProcessingException {
        Optional<CacheData> optionalCacheData = redisRepository.findById(String.valueOf(id));

        if (optionalCacheData.isPresent()) {
            String gameString = optionalCacheData.get().getValue();
            TypeReference<Game> mapType = new TypeReference<Game>() {};
            Game game = mapper.readValue(gameString, mapType);
            return game;
        } else {
            var game = gameRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Game with id " + id + " is not found in DB"));

            saveGameToRedisById(game);
            return game;
        }
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public Game updateGame(Game game, Integer id) throws JsonProcessingException {
        var gameFromDb = findGameById(id);

        gameFromDb.setInfoId(game.getInfoId());
        gameFromDb.setBoard(game.getBoard());
        gameFromDb.setActive(game.getActive());
        gameFromDb.setTokenForWhitePlayer(game.getTokenForWhitePlayer());
        gameFromDb.setTokenForBlackPlayer(game.getTokenForBlackPlayer());
        saveGame(gameFromDb);
        saveGameToRedisById(gameFromDb);
        saveGameToRedisByToken(gameFromDb);

        return gameFromDb;
    }

    public void deleteGame(Integer id) throws JsonProcessingException {
        Game game = findGameById(id);

        redisRepository.deleteById(String.valueOf(id));
        redisRepository.deleteById(game.getTokenForBlackPlayer().toString());
        redisRepository.deleteById(game.getTokenForWhitePlayer().toString());
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

    public GameResponse getCurrentGame(UUID token) throws JsonProcessingException {
        Optional<CacheData> optionalCacheData = redisRepository.findById(token.toString());
        Game game = null;
        Color currentColor = null;

        if (optionalCacheData.isPresent()) {
            String gameString = optionalCacheData.get().getValue();
            TypeReference<Game> mapType = new TypeReference<Game>() {};
            game = mapper.readValue(gameString, mapType);
            currentColor = game.getTokenForBlackPlayer().equals(token) ? Color.BLACK : Color.WHITE;
        } else {
            game = gameRepository.getGameByToken(token);
            currentColor = game.getTokenForBlackPlayer().equals(token) ? Color.BLACK : Color.WHITE;
            saveGameToRedisByToken(game);
        }

        return GameResponse.builder()
                .info(game.getInfoId())
                .tokenForBlackPlayer(game.getTokenForBlackPlayer())
                .tokenForWhitePlayer(game.getTokenForWhitePlayer())
                .board(mapper.readValue(game.getBoard(), ShortFigureName[][].class))
                .active(game.getActive())
                .currentColor(currentColor)
                .build();
    }

    public void saveGameToRedisById(Game game) throws JsonProcessingException {
        String gameAsJsonString = mapper.writeValueAsString(game);
        CacheData cacheData = new CacheData(String.valueOf(game.getId()), gameAsJsonString);
        redisRepository.save(cacheData);
    }

    public void saveGameToRedisByToken(Game game) throws JsonProcessingException {
        String gameAsJsonString = mapper.writeValueAsString(game);
        CacheData cacheData = new CacheData(game.getTokenForBlackPlayer().toString(), gameAsJsonString);
        CacheData cacheData2 = new CacheData(game.getTokenForWhitePlayer().toString(), gameAsJsonString);
        redisRepository.saveAll(List.of(cacheData, cacheData2));
    }

    public void move(MoveRequest moveRequest) {
        //fromX FromY -> board.getCell
        //ToY ToX -> board.getCell
        // get Figure fromCell ( only from) if null or color is not Active throw error
        //figure.canMove -> false (throw error)
        //figure.move
        // save new board version and change active color to oposite
        //calculate gameInfo (return Game Info Object in response)
        //socket for second player from and to coordinates and new active color and current game info
    }

    public void giveUp(UUID token) {

    }
}