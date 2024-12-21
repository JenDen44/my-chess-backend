package com.chess.jnd.service;

import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
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
class GameCommonServiceTest {
    @Mock
    GameCommonService gameCommonService;

    @Mock
    HttpServletRequest request;


    @DisplayName("Test Save New Game")
    @Test
    void saveGame() {
        GameRedis gameRedis = new GameRedis();

         given(gameCommonService.saveGame(any(GameRedis.class))).willReturn(gameRedis);

        GameRedis gameFromService = gameCommonService.saveGame(gameRedis);

        then(gameCommonService).should().saveGame(any(GameRedis.class));
        assertThat(gameFromService).isNotNull();
    }

    @DisplayName("Test Get Game By ID")
    @Test
    void findGameById() {
        GameRedis game = new GameRedis();

        given(gameCommonService.findGameById(anyInt())).willReturn(game);

        GameRedis gameFromService = gameCommonService.findGameById(1);

        then(gameCommonService).should().findGameById(anyInt());
        assertThat(gameFromService).isNotNull();
    }

    @DisplayName("Test Get Game By Token")
    @Test
    void findGameByToken() throws JsonProcessingException {
        GameRedis game = new GameRedis();
        String token = "hehdujwisijswijsiw";
        game.setTokenForBlackPlayer(token);

        given(gameCommonService.findGameByToken(any(String.class))).willReturn(game);

        GameRedis gameFromService = gameCommonService.findGameByToken(token);

        then(gameCommonService).should().findGameByToken(any(String.class));
        assertThat(gameFromService).isNotNull();
    }

    @DisplayName("Test Delete Game By ID")
    @Test
    void deleteGame() {
        GameRedis gameRedis = new GameRedis();
        gameRedis.setId(1);

        gameCommonService.deleteGame(1);
        then(gameCommonService).should().deleteGame(anyInt());
    }

    @DisplayName("Test Create Board")
    @Test
    void initBoard() {
        ShortFigureName shortNames [][] = new ShortFigureName[0][0];

        given(gameCommonService.initBoard()).willReturn(shortNames);

        shortNames = gameCommonService.initBoard();

        then(gameCommonService).should().initBoard();
        assertThat(shortNames).isNotNull();

    }

    @DisplayName("Test Create Game")
    @Test
    void createGame() throws JsonProcessingException {
        GameResponse gameResponse = new GameResponse();
        CreateGameRequest createGameRequest = new CreateGameRequest();

        given(gameCommonService.createGame(any(CreateGameRequest.class))).willReturn(gameResponse);

        GameResponse gameResponseFromService = gameCommonService.createGame(createGameRequest);

        then(gameCommonService).should().createGame(any(CreateGameRequest.class));
        assertThat(gameResponseFromService).isNotNull();
    }

    @DisplayName("Test Get Current Game")
    @Test
    void getCurrentGame() throws JsonProcessingException {
        GameResponse gameResponse = new GameResponse();

        given(gameCommonService.getCurrentGame(any(HttpServletRequest.class))).willReturn(gameResponse);

        GameResponse gameResponseFromService = gameCommonService.getCurrentGame(request);

        then(gameCommonService).should().getCurrentGame(any(HttpServletRequest.class));
        assertThat(gameResponseFromService).isNotNull();
    }

    @DisplayName("Test Get Opposite Token")
    @Test
    void getOppositeToken() {
        GameRedis game = new GameRedis();
        String token = "hehdujwisijswijsiw";
        String oppositeToken = "jdiwdowkdwgsgs";
        game.setTokenForBlackPlayer(token);
        game.setTokenForWhitePlayer(token);

        given(gameCommonService.getOppositeToken(any(GameRedis.class), any(String.class))).willReturn(oppositeToken);

        String oppositeTokenFromService = gameCommonService.getOppositeToken(game, token);

        then(gameCommonService).should().getOppositeToken(any(GameRedis.class), any(String.class));
        assertThat(oppositeTokenFromService).isNotNull();
        assertEquals(oppositeTokenFromService, oppositeToken);
    }

    @DisplayName("Test Move")
    @Test
    void move() throws JsonProcessingException {
        MoveRequest moveRequest = new MoveRequest();
        GameInfoResponse gameInfoResponse = new GameInfoResponse();

        given(gameCommonService.move(any(HttpServletRequest.class), any(MoveRequest.class))).willReturn(gameInfoResponse);

        GameInfoResponse GameInfoResponseFromService = gameCommonService.move(request, moveRequest);

        then(gameCommonService).should().move(any(HttpServletRequest.class), any(MoveRequest.class));
        assertThat(GameInfoResponseFromService).isNotNull();
    }

    @DisplayName("Test Give up")
    @Test
    void giveUp() throws JsonProcessingException {
        GameInfoResponse gameInfoResponse = new GameInfoResponse();

        given(gameCommonService.giveUp(any(HttpServletRequest.class))).willReturn(gameInfoResponse);

        GameInfoResponse GameInfoResponseFromService = gameCommonService.giveUp(request);

        then(gameCommonService).should().giveUp(any(HttpServletRequest.class));
        assertThat(GameInfoResponseFromService).isNotNull();
    }
}