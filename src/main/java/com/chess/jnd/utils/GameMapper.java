package com.chess.jnd.utils;

import com.chess.jnd.entity.Board;
import com.chess.jnd.entity.Game;
import com.chess.jnd.entity.GameRedis;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    private final ObjectMapper mapper;

    @Autowired
    public GameMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Game mapToGame(GameRedis gameRedis) throws JsonProcessingException {
        return Game.builder()
                .id(gameRedis.getId())
                .tokenForWhitePlayer(gameRedis.getTokenForWhitePlayer())
                .tokenForBlackPlayer(gameRedis.getTokenForBlackPlayer())
                .active(gameRedis.getActive())
                .board(mapper.writeValueAsString(gameRedis.getBoard().getShortNames()))
                .infoId(gameRedis.getInfoId())
                .build();

    }

    public GameRedis mapToGameRedis(Game game) throws JsonProcessingException {
        return GameRedis.builder()
                .id(game.getId())
                .tokenForWhitePlayer(game.getTokenForWhitePlayer())
                .tokenForBlackPlayer(game.getTokenForBlackPlayer())
                .active(game.getActive())
                .board(new Board(mapper.readValue(game.getBoard(), ShortFigureName[][].class)))
                .infoId(game.getInfoId())
                .build();
    }
}