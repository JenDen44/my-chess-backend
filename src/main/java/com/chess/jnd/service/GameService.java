package com.chess.jnd.service;

import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.chess.jnd.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final GameInfoService gameInfoService;
    private final ObjectMapper mapper;

    @Autowired
    public GameService(GameRepository gameRepository, GameInfoService gameInfoService, ObjectMapper mapper) {
        this.gameRepository = gameRepository;
        this.gameInfoService = gameInfoService;
        this.mapper = mapper;
    }

    public GameResponse createNewGame(CreateGameRequest gameRequest) throws JsonProcessingException {
        ShortFigureName[][] shortFigureNames = initBoard();
        String board  = mapper.writeValueAsString(shortFigureNames);
        Game game = new Game(board);
        gameInfoService.saveGameInfo(game.getInfoId());
        Game savedGameToDB = saveGame(game);

        return GameResponse.builder()
                .info(savedGameToDB.getInfoId())
                .tokenForBlackPlayer(savedGameToDB.getTokenForBlackPlayer())
                .tokenForWhitePlayer(savedGameToDB.getTokenForWhitePlayer())
                .board(mapper.readValue(savedGameToDB.getBoard(), ShortFigureName[][].class))
                .active(savedGameToDB.getActive())
                .currentColor(gameRequest.getColor())
                .build();
    }

    public Game findGameById(Integer id) {
        var game = gameRepository.findById(id)
                .orElseThrow (() -> new RuntimeException("Game with id " + id + " is not found in DB"));

        return game;
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public Game updateGame(Game game, Integer id) {
        var gameFromDb = findGameById(id);

        gameFromDb.setInfoId(game.getInfoId());
        gameFromDb.setBoard(game.getBoard());
        gameFromDb.setActive(game.getActive());
        gameFromDb.setTokenForWhitePlayer(game.getTokenForWhitePlayer());
        gameFromDb.setTokenForBlackPlayer(game.getTokenForBlackPlayer());
        saveGame(gameFromDb);

        return gameFromDb;
    }

    public void deleteGame(Integer id) {
        findGameById(id);
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
        Game game = gameRepository.getGameByToken(token);
        Color currentColor = game.getTokenForBlackPlayer().equals(token) ? Color.BLACK : Color.WHITE;

        return GameResponse.builder()
                .info(game.getInfoId())
                .tokenForBlackPlayer(game.getTokenForBlackPlayer())
                .tokenForWhitePlayer(game.getTokenForWhitePlayer())
                .board(mapper.readValue(game.getBoard(), ShortFigureName[][].class))
                .active(game.getActive())
                .currentColor(currentColor)
                .build();
    }
}