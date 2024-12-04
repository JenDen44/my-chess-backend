package com.chess.jnd.utils;

import com.chess.jnd.entity.Board;
import com.chess.jnd.entity.Game;
import com.chess.jnd.entity.GameRedis;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface  GameToRedisGameMapper {
    ObjectMapper mapper = new ObjectMapper();

    @Mapping(target = "board", qualifiedByName = "boardToString", source = "board")
    abstract Game gameRedisToGame(GameRedis game);

    @Mapping(target = "board", qualifiedByName = "stringToBoard", source = "board")
    abstract GameRedis gameToGameRedis(Game game);

    @Named("stringToBoard")
     default Board stringToBoard(String board) throws JsonProcessingException {
        return new Board(mapper.readValue(board, ShortFigureName[][].class));
    }

    @Named("boardToString")
     default String boardToString(Board board) throws JsonProcessingException {
       return mapper.writeValueAsString(board.getShortNames());
    }
}
