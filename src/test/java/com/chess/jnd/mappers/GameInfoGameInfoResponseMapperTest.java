package com.chess.jnd.mappers;

import com.chess.jnd.entity.GameInfo;
import com.chess.jnd.entity.GameInfoResponse;
import com.chess.jnd.entity.GameResult;
import com.chess.jnd.entity.GameStatus;
import com.chess.jnd.mappers.GameInfoGameInfoResponseMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoGameInfoResponseMapperTest {

    private final GameInfoGameInfoResponseMapperImpl gameInfoMapper = new GameInfoGameInfoResponseMapperImpl();

    @DisplayName("Test Map GameInfoResponse to GameInfo")
    @Test
    void gameInfoResponseToGameInfo() {
        GameInfoResponse gameInfoResponse = new GameInfoResponse(GameStatus.IN_PROCESS, GameResult.DRAW);

        GameInfo gameInfo = gameInfoMapper.gameInfoResponseToGameInfo(gameInfoResponse);

        assertEquals(gameInfo.getStatus(), gameInfoResponse.getStatus());
        assertEquals(gameInfo.getDetail(), gameInfoResponse.getDetail());
    }

    @DisplayName("Test Map GameInfo to GameInfoResponse")
    @Test
    void gameInfoToGameInfoResponse() {
        GameInfo gameInfo = new GameInfo(GameStatus.FINISHED);

        GameInfoResponse gameInfoResponse = gameInfoMapper.gameInfoToGameInfoResponse(gameInfo);

        assertEquals(gameInfo.getStatus(), gameInfoResponse.getStatus());
        assertEquals(gameInfo.getDetail(), gameInfoResponse.getDetail());
    }
}