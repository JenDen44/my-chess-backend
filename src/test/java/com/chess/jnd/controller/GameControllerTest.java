package com.chess.jnd.controller;

import com.chess.jnd.ParameterFabric;
import com.chess.jnd.entity.*;
import com.chess.jnd.error_handling.GameNotFoundException;
import com.chess.jnd.error_handling.GameWrongDataException;
import com.chess.jnd.service.GameCommonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameCommonService service;

    private MockHttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockRequest = ParameterFabric.createMockRequest();
    }

    @DisplayName("Test Create new Game")
    @Test
    public void createNewGame() throws Exception {
        GameResponse gameResponse =  ParameterFabric.createGameResponse();
        GameInfoResponse gameInfoResponse =  ParameterFabric.createGameInfoResponse();
        gameResponse.setInfo(gameInfoResponse);
        CreateGameRequest createGameRequest = ParameterFabric.createGameRequest();

        given(service.createGame(any(CreateGameRequest.class))).willReturn(gameResponse);

        var response = mvc.perform(
                post("/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.info.status", is(GameStatus.IN_PROCESS.getStatus())))
                .andExpect(jsonPath("$.active", is(Color.BLACK.getColor())));

        verify(service).createGame(any(CreateGameRequest.class));

    }

    @DisplayName("Test Get Current Game")
    @Test
    void currentGame() throws Exception {
        GameResponse gameResponse = ParameterFabric.createGameResponse();
        GameInfoResponse gameInfoResponse =  ParameterFabric.createGameInfoResponse();
        gameResponse.setInfo(gameInfoResponse);

        given(service.getCurrentGame(any(HttpServletRequest.class))).willReturn(gameResponse);

        var response = mvc.perform(get("/game").headers(ParameterFabric.createHttpHeader()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.tokenForWhitePlayer", is(gameResponse.getTokenForWhitePlayer())))
                .andExpect(jsonPath("$.tokenForBlackPlayer", is(gameResponse.getTokenForBlackPlayer())))
                .andExpect(jsonPath("$.active", is(gameResponse.getActive().getColor())))
                .andExpect(jsonPath("$.info.status", is(gameResponse.getInfo().getStatus().getStatus())))
                .andExpect(jsonPath("$.currentColor", is(gameResponse.getCurrentColor().getColor())));

        verify(service).getCurrentGame(any(MockHttpServletRequest.class));
    }

    @DisplayName("Test Get Current Game with GameNotFoundException")
    @Test
    void currentGameNotFound() throws Exception {
        GameResponse gameResponse = ParameterFabric.createGameResponse();
        GameInfoResponse gameInfoResponse =  ParameterFabric.createGameInfoResponse();
        gameResponse.setInfo(gameInfoResponse);

        given(service.getCurrentGame(any(HttpServletRequest.class))).willThrow(GameNotFoundException.class);

        var response = mvc.perform(get("/game").headers(ParameterFabric.createHttpHeader()));

        response.andExpect(status().isNotFound())
                .andDo(print());

        verify(service).getCurrentGame(any(MockHttpServletRequest.class));
    }

    @DisplayName("Test Perform Move")
    @Test
    void move() throws Exception {
        MoveRequest moveRequest = ParameterFabric.createMoveRequest(1,2,3,1);
        GameInfoResponse gameInfoResponse = ParameterFabric.createGameInfoResponse();

        given(service.move(any(HttpServletRequest.class), any(MoveRequest.class))).willReturn(gameInfoResponse);

        var response = mvc.perform(post("/game/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveRequest))
                .headers(ParameterFabric.createHttpHeader()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status", is(gameInfoResponse.getStatus().getStatus())));


        verify(service).move(any(HttpServletRequest.class), any(MoveRequest.class));
    }

    @DisplayName("Test Perform Move with GameWrongDataException")
    @Test
    void moveWrongDateException() throws Exception {
        MoveRequest moveRequest = ParameterFabric.createMoveRequest(1,2,3,1);
        GameInfoResponse gameInfoResponse = ParameterFabric.createGameInfoResponse();

        given(service.move(any(HttpServletRequest.class), any(MoveRequest.class))).willThrow(GameWrongDataException.class);

        var response = mvc.perform(post("/game/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveRequest))
                .headers(ParameterFabric.createHttpHeader()));

        response.andExpect(status()
                .isUnprocessableEntity())
                .andDo(print());

        verify(service).move(any(HttpServletRequest.class), any(MoveRequest.class));
    }

    @DisplayName("Test Give Up")
    @Test
    void giveUp() throws Exception {
        GameInfoResponse gameInfoResponse = ParameterFabric.createGameInfoResponse();

        given(service.giveUp(any(HttpServletRequest.class))).willReturn(gameInfoResponse);

        var response = mvc.perform(post("/game/give-up").headers(ParameterFabric.createHttpHeader()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status", is(gameInfoResponse.getStatus().getStatus())))
                .andExpect(jsonPath("$.detail", is(gameInfoResponse.getDetail())));

        verify(service).giveUp(any(HttpServletRequest.class));
    }

    @DisplayName("Test Offer Draw")
    @Test
    void offerDraw() throws Exception {
        var response = mvc.perform(post("/game/draw")
                .headers(ParameterFabric.createHttpHeader()));

        response.andExpect(status().isOk())
                .andDo(print());

        verify(service).offerDraw(any(HttpServletRequest.class));
    }

    @DisplayName("Test Draw")
    @Test
    void draw() throws Exception {
        var response = mvc.perform(post("/game/draw/{answer}", true)
                .headers(ParameterFabric.createHttpHeader()));

        response.andExpect(status().isOk())
                .andDo(print());

        verify(service).draw(any(HttpServletRequest.class), anyBoolean());
    }
}