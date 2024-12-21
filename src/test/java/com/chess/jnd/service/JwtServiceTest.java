package com.chess.jnd.service;

import com.chess.jnd.entity.Color;
import com.chess.jnd.entity.GameRedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    JwtService jwtService;

    @Mock
    HttpServletRequest request;

    @Test
    void generateCustomToken() throws JsonProcessingException {
        GameRedis gameRedis = new GameRedis();
        gameRedis.setId(1);
        gameRedis.setTokenForWhitePlayer("hdhdhsdjadja");

        given(jwtService.generateCustomToken(anyInt(), any(Color.class))).willReturn(gameRedis.getTokenForWhitePlayer());

        String token = jwtService.generateCustomToken(1, Color.WHITE);

        then(jwtService).should().generateCustomToken(anyInt(), any(Color.class));
        assertThat(token).isNotNull();
    }

    @Test
    void getGameId() throws JsonProcessingException {
        GameRedis gameRedis = new GameRedis();
        gameRedis.setId(1);
        gameRedis.setTokenForWhitePlayer("hdhdhsdjadja");

        given(jwtService.getGameId(any(String.class))).willReturn(1);

        Integer id = jwtService.getGameId(gameRedis.getTokenForWhitePlayer());

        then(jwtService).should().getGameId(any(String.class));
        assertThat(id).isNotNull();
    }

    @Test
    void getColor() throws JsonProcessingException {
        GameRedis gameRedis = new GameRedis();
        gameRedis.setTokenForWhitePlayer("hdhdhsdjadja");
        gameRedis.setActive(Color.BLACK);

        given(jwtService.getColor(any(String.class))).willReturn(gameRedis.getActive());

        Color color = jwtService.getColor(gameRedis.getTokenForWhitePlayer());

        then(jwtService).should().getColor(any(String.class));
        assertThat(color).isNotNull();
        assertEquals(color, gameRedis.getActive());
    }

    @Test
    void resolveToken() {
        GameRedis gameRedis = new GameRedis();
        gameRedis.setTokenForWhitePlayer("hdhdhsdjadja");
        gameRedis.setActive(Color.BLACK);

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());

        String token = jwtService.resolveToken(request);

        then(jwtService).should().resolveToken(any(HttpServletRequest.class));
        assertThat(token).isNotNull();
        assertEquals(token, gameRedis.getTokenForWhitePlayer());
    }
}