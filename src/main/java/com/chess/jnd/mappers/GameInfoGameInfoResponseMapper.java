package com.chess.jnd.mappers;

import com.chess.jnd.entity.GameInfo;
import com.chess.jnd.entity.GameInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameInfoGameInfoResponseMapper {

    ObjectMapper mapper = new ObjectMapper();

    abstract GameInfo gameInfoResponseToGameInfo(GameInfoResponse gameInfoDto);

    abstract GameInfoResponse gameInfoToGameInfoResponse(GameInfo gameInfo);
}
