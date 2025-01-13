package com.chess.jnd.mappers;

import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GameToRedisGameMapper {
    ObjectMapper mapper = new ObjectMapper();

    @Mapping(target = "board", qualifiedByName = "boardToString", source = "board")
    @Mapping(target = "passantCell", qualifiedByName = "passantCellToString", source = "passantCell")
    @Mapping(target = "prevStep", qualifiedByName = "prevStepToString", source = "prevStep")
    abstract Game gameRedisToGame(GameRedis game);

    @Mapping(target = "board", qualifiedByName = "stringToBoard", source = "board")
    @Mapping(target = "passantCell", qualifiedByName = "stringToPassantCell", source = "passantCell")
    @Mapping(target = "prevStep", qualifiedByName = "stringToPrevStep", source = "prevStep")
    abstract GameRedis gameToGameRedis(Game game);

    @Named("stringToBoard")
     default ShortFigureName[][] stringToBoard(String board) throws JsonProcessingException {
        return mapper.readValue(board, ShortFigureName[][].class);
    }

    @Named("boardToString")
     default String boardToString(ShortFigureName[][] board) throws JsonProcessingException {
       return mapper.writeValueAsString(board);
    }

    @Named("stringToPassantCell")
    default PassantCell stringToPassantCell(String passantCell) throws JsonProcessingException {
        if (passantCell == null) return null;

        return mapper.readValue(passantCell, PassantCell.class);
    }

    @Named("passantCellToString")
    default String passantCellToString(PassantCell passantCell) throws JsonProcessingException {
        return mapper.writeValueAsString(passantCell);
    }

    @Named("stringToPrevStep")
    default PrevStep stringToPrevStep(String prevStep) throws JsonProcessingException {
        if (prevStep == null) return null;

        return mapper.readValue(prevStep, PrevStep.class);
    }

    @Named("prevStepToString")
    default String prevStepToString(PrevStep prevStep) throws JsonProcessingException {
        return mapper.writeValueAsString(prevStep);
    }
}
