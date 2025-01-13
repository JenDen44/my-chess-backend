package com.chess.jnd.mappers;

import com.chess.jnd.ParameterFabric;
import com.chess.jnd.entity.Game;
import com.chess.jnd.entity.GameRedis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameToRedisGameMapperTest {

    private final GameToRedisGameMapperImpl gameMapper = new GameToRedisGameMapperImpl();

    @DisplayName("Test Map GameRedis to Game")
    @Test
    void gameRedisToGame() {
        GameRedis gameRedis = ParameterFabric.createRedisGameWithBoard();

        Game game = gameMapper.gameRedisToGame(gameRedis);

        assertEquals(gameRedis.getId(), game.getId());
        assertEquals(gameRedis.getActive(), game.getActive());
        assertEquals(gameRedis.getTokenForWhitePlayer(), game.getTokenForWhitePlayer());
        assertEquals(gameRedis.getTokenForBlackPlayer(), game.getTokenForBlackPlayer());
        assertEquals(gameRedis.getGameInfo().getStatus(), game.getGameInfo().getStatus());
    }

    @DisplayName("Test Map Game to GameRedis")
    @Test
    void gameToGameRedis() {
        Game game = ParameterFabric.createGameWithBoard();

        GameRedis gameRedis = gameMapper.gameToGameRedis(game);

        assertEquals(gameRedis.getId(), game.getId());
        assertEquals(gameRedis.getActive(), game.getActive());
        assertEquals(gameRedis.getTokenForWhitePlayer(), game.getTokenForWhitePlayer());
        assertEquals(gameRedis.getTokenForBlackPlayer(), game.getTokenForBlackPlayer());
        assertEquals(gameRedis.getGameInfo().getStatus(), game.getGameInfo().getStatus());
    }
}