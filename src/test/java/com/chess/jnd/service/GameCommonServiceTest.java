package com.chess.jnd.service;

import com.chess.jnd.ParameterFabric;
import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.chess.jnd.error_handling.GameWrongDataException;
import com.chess.jnd.notification.GameNotificationService;
import com.chess.jnd.mappers.GameInfoGameInfoResponseMapper;
import com.chess.jnd.mappers.GameToRedisGameMapperImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameCommonServiceTest {

    @InjectMocks
    private GameCommonService gameCommonService;

    @Mock
    private GameRedisService gameRedisService;

    @Mock
    private GameService gameService;

    @Mock
    private GameInfoService gameInfoService;

    @Mock
    private JwtService jwtService;

    @Mock
    private GameToRedisGameMapperImpl gameMapper;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private GameNotificationService notificationService;

    @Mock
    private GameInfoGameInfoResponseMapper gameInfoMapper;


    @DisplayName("Test Save New Game")
    @Test
    void saveGame() {
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        Game game = ParameterFabric.createGame();

        given(gameMapper.gameRedisToGame(any(GameRedis.class))).willReturn(game);
        given(gameService.save(any(Game.class))).willReturn(game);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);

        GameRedis gameFromService = gameCommonService.saveGame(gameRedis);

        verify(gameMapper).gameRedisToGame(any(GameRedis.class));
        verify(gameService).save(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));

        assertThat(gameFromService).isNotNull();
        assertEquals(gameFromService.getActive(), Color.WHITE);
        assertEquals(gameFromService.getGameInfo().getStatus(), GameStatus.IN_PROCESS);

    }

    @DisplayName("Test Get Game By ID")
    @Test
    void findGameByIdWithRedisService() {
        GameRedis gameRedis = ParameterFabric.createRedisGame();

        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        GameRedis gameFromService = gameCommonService.findGameById(gameRedis.getId());

        verify(gameRedisService).get(anyInt());

        assertThat(gameFromService).isNotNull();
        assertEquals(gameFromService.getId(), gameRedis.getId());
    }

    @DisplayName("Test Get Game By ID")
    @Test
    void findGameByIdWithGameService() {
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        Game game = ParameterFabric.createGame();

        given(gameService.get(anyInt())).willReturn(game);
        given(gameMapper.gameToGameRedis(any(Game.class))).willReturn(gameRedis);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);

        GameRedis gameFromService = gameCommonService.findGameById(gameRedis.getId());

        verify(gameService).get(anyInt());
        verify(gameMapper).gameToGameRedis(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));

        assertThat(gameFromService).isNotNull();
        assertEquals(gameFromService.getId(), gameRedis.getId());
    }

    @DisplayName("Test Get Game By Token")
    @Test
    void findGameByToken() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGame();

        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        GameRedis gameFromService = gameCommonService.findGameByToken(gameRedis.getTokenForWhitePlayer());

        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());

        assertThat(gameFromService).isNotNull();
        assertEquals(gameFromService.getTokenForWhitePlayer(), gameRedis.getTokenForWhitePlayer());
    }

    @DisplayName("Test Delete Game By ID")
    @Test
    void deleteGame() {
        gameCommonService.deleteGame(1);

        verify(gameRedisService).delete(anyInt());
        verify(gameService).delete(anyInt());
    }

    @DisplayName("Test Create Board")
    @Test
    void initBoard() {
        ShortFigureName shortNames [][];

        shortNames = gameCommonService.initBoard();

        assertThat(shortNames).isNotNull();
        assertEquals(shortNames.length,  8);

    }

    @DisplayName("Test Create Game")
    @Test
    void createGame() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGameWithBoard();
        Game game = ParameterFabric.createGameWithBoard();
        GameInfoResponse gameInfoResponse = ParameterFabric.createGameInfoResponse();
        CreateGameRequest createGameRequest = ParameterFabric.createGameRequest();

        given(mapper.writeValueAsString(any(ShortFigureName[][].class))).willReturn(game.getBoard());
        given(gameService.save(any(Game.class))).willReturn(game);
        given(jwtService.generateCustomToken(any(), any(Color.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(gameMapper.gameToGameRedis(any(Game.class))).willReturn(gameRedis);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);
        given(gameInfoMapper.gameInfoToGameInfoResponse(any(GameInfo.class))).willReturn(gameInfoResponse);
        given(jwtService.getGameId(anyString())).willReturn(game.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        GameResponse gameResponseFromService = gameCommonService.createGame(createGameRequest);

        verify(mapper).writeValueAsString(any(ShortFigureName[][].class));
        verify(gameService, times(2)).save(any(Game.class));
        verify(jwtService, times(2)).generateCustomToken(any(), any(Color.class));
        verify(gameMapper).gameToGameRedis(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));
        verify(gameInfoMapper).gameInfoToGameInfoResponse(any(GameInfo.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());

        assertThat(gameResponseFromService).isNotNull();
        assertEquals(gameResponseFromService.getTokenForBlackPlayer(), gameRedis.getTokenForBlackPlayer());
        assertEquals(gameResponseFromService.getTokenForBlackPlayer(), gameRedis.getTokenForBlackPlayer());
        assertEquals(gameResponseFromService.getActive(), Color.WHITE);
        assertEquals(gameResponseFromService.getInfo().getStatus(), GameStatus.IN_PROCESS);
    }

    @DisplayName("Test Get Current Game")
    @Test
    void getCurrentGame() throws JsonProcessingException {
        GameInfoResponse gameInfoResponse = ParameterFabric.createGameInfoResponse();
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);
        given(gameInfoMapper.gameInfoToGameInfoResponse(any(GameInfo.class))).willReturn(gameInfoResponse);
        given(jwtService.getColor(anyString())).willReturn(gameRedis.getActive());

        GameResponse gameResponseFromService = gameCommonService.getCurrentGame(mockRequest);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());
        verify(gameInfoMapper).gameInfoToGameInfoResponse(any(GameInfo.class));
        verify(jwtService).getColor(anyString());

        assertThat(gameResponseFromService).isNotNull();
        assertEquals(gameResponseFromService.getInfo().getStatus(), gameRedis.getGameInfo().getStatus());
        assertEquals(gameResponseFromService.getTokenForBlackPlayer(), gameRedis.getTokenForBlackPlayer());
        assertEquals(gameResponseFromService.getTokenForWhitePlayer(), gameRedis.getTokenForWhitePlayer());
    }

    @DisplayName("Test Get Opposite Token")
    @Test
    void getOppositeToken() {
        GameRedis gameRedis = ParameterFabric.createRedisGame();

        String oppositeToken = gameCommonService.getOppositeToken(gameRedis, gameRedis.getTokenForWhitePlayer());

        assertThat(oppositeToken).isNotNull();
        assertEquals(oppositeToken, gameRedis.getTokenForBlackPlayer());
    }

    @DisplayName("Test Move Game in Progress, No Draw and No Mate")
    @Test
    void moveGameInProgress() throws JsonProcessingException {
        MoveRequest moveRequest = ParameterFabric.createMoveRequest(7,7,6,5);
        GameRedis gameRedis = ParameterFabric.createRedisGameWithBoard();
        Game game = ParameterFabric.createGameWithBoard();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);
        given(gameMapper.gameRedisToGame(any(GameRedis.class))).willReturn(game);
        given(gameService.save(any(Game.class))).willReturn(game);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);


        GameInfoResponse gameInfoResponseFromService = gameCommonService.move(mockRequest, moveRequest);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService, times(2)).getGameId(anyString());
        verify(gameRedisService, times(2)).get(anyInt());
        verify(gameMapper).gameRedisToGame(any(GameRedis.class));
        verify(gameService).save(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));
        verify(notificationService).sendNotificationForMove(any(MoveNotify.class), anyString());

        assertThat(gameInfoResponseFromService).isNotNull();
        assertEquals(gameInfoResponseFromService.getStatus(), GameStatus.IN_PROCESS);
        assertNull(gameInfoResponseFromService.getDetail());
    }

    @DisplayName("Test Move Game Mate")
    @Test
    void moveWithCheckmate() throws JsonProcessingException {
        MoveRequest moveRequest = ParameterFabric.createMoveRequest(5,7,0,0);
        GameRedis gameRedis = ParameterFabric.createRedisGameForCheckMateOrDraw();
        Game game = ParameterFabric.createGameForCheckMate();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);
        given(gameInfoService.save(any(GameInfo.class))).willReturn(gameRedis.getGameInfo());
        given(gameMapper.gameRedisToGame(any(GameRedis.class))).willReturn(game);
        given(gameService.save(any(Game.class))).willReturn(game);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);

        GameInfoResponse GameInfoResponseFromService = gameCommonService.move(mockRequest, moveRequest);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService, times(2)).getGameId(anyString());
        verify(gameRedisService, times(2)).get(anyInt());
        verify(gameInfoService).save(any(GameInfo.class));
        verify(notificationService).sendNotificationForGameStatus(any(GameStatusNotify.class), anyString(), anyString());
        verify(gameMapper).gameRedisToGame(any(GameRedis.class));
        verify(gameService).save(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));
        verify(notificationService).sendNotificationForMove(any(MoveNotify.class), anyString());

        assertThat(GameInfoResponseFromService).isNotNull();
        assertEquals(GameInfoResponseFromService.getStatus(), GameStatus.FINISHED);
        assertEquals(GameInfoResponseFromService.getDetail(), GameResult.BLACK);
    }

    @DisplayName("Test Move Game Draw")
    @Test
    void moveDraw() throws JsonProcessingException {
        MoveRequest moveRequest = ParameterFabric.createMoveRequest(5,5,0,6);
        GameRedis gameRedis = ParameterFabric.createRedisGameForCheckMateOrDraw();
        Game game = ParameterFabric.createGame();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);
        given(gameInfoService.save(any(GameInfo.class))).willReturn(gameRedis.getGameInfo());
        given(gameMapper.gameRedisToGame(any(GameRedis.class))).willReturn(game);
        given(gameService.save(any(Game.class))).willReturn(game);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);

        GameInfoResponse gameInfoResponseFromService = gameCommonService.move(mockRequest, moveRequest);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService, times(2)).getGameId(anyString());
        verify(gameRedisService, times(2)).get(anyInt());
        verify(gameInfoService).save(any(GameInfo.class));
        verify(notificationService).sendNotificationForGameStatus(any(GameStatusNotify.class), anyString(), anyString());
        verify(gameMapper).gameRedisToGame(any(GameRedis.class));
        verify(gameService).save(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));
        verify(notificationService).sendNotificationForMove(any(MoveNotify.class), anyString());

        assertThat(gameInfoResponseFromService).isNotNull();
        assertEquals(gameInfoResponseFromService.getDetail(), GameResult.DRAW);
        assertEquals(gameInfoResponseFromService.getStatus(), GameStatus.FINISHED);
    }

    @DisplayName("Test Move Game Finished with Error")
    @Test
    void moveGameFinishedError() throws JsonProcessingException {
        MoveRequest moveRequest = ParameterFabric.createMoveRequest(7,7,6,5);
        GameRedis gameRedis = ParameterFabric.createRedisGameWithBoardWithStatusFinished();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();
        String expectedMessage = "The game is already finished";


        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForBlackPlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        Exception exception = assertThrows(GameWrongDataException.class, () -> {
            gameCommonService.move(mockRequest, moveRequest);
        });

        String actualMessage = exception.getMessage();

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Test Give up")
    @Test
    void giveUp() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        Game game = ParameterFabric.createGame();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);
        given(jwtService.getColor(anyString())).willReturn(gameRedis.getActive());
        given(gameInfoService.save(any(GameInfo.class))).willReturn(gameRedis.getGameInfo());
        given(gameMapper.gameRedisToGame(any(GameRedis.class))).willReturn(game);
        given(gameService.save(any(Game.class))).willReturn(game);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);

        GameInfoResponse gameInfoResponseFromService = gameCommonService.giveUp(mockRequest);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());
        verify(jwtService).getColor(anyString());
        verify(gameInfoService).save(any(GameInfo.class));
        verify(notificationService).sendNotificationForGameStatus(any(GameStatusNotify.class), anyString(), anyString());
        verify(gameMapper).gameRedisToGame(any(GameRedis.class));
        verify(gameService).save(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));

        assertThat(gameInfoResponseFromService).isNotNull();
        assertEquals(gameInfoResponseFromService.getStatus(), GameStatus.FINISHED);
        assertEquals(gameInfoResponseFromService.getDetail(), GameResult.BLACK);
    }

    @DisplayName("Test Give up With Error")
    @Test
    void giveUpWithError() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGameWithBoardWithStatusFinished();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();
        String expectedMessage = "The game is already finished";

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForBlackPlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);


        Exception exception = assertThrows(GameWrongDataException.class, () -> {
            gameCommonService.giveUp(mockRequest);
        });

        String actualMessage = exception.getMessage();

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Test Change Game Status and Notify Players")
    @Test
    void changeGameStatusAndNotifyPlayers() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGame();

        given(gameInfoService.save(any(GameInfo.class))).willReturn(gameRedis.getGameInfo());

        gameCommonService.changeGameStatusAndNotifyPlayers(gameRedis.getGameInfo(), GameResult.BLACK, GameStatus.FINISHED, gameRedis.getTokenForWhitePlayer());

        verify(gameInfoService).save(any(GameInfo.class));
        verify(notificationService).sendNotificationForGameStatus(any(GameStatusNotify.class), anyString());
    }

    @DisplayName("Test Offer Draw")
    @Test
    void offerDraw() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        gameCommonService.offerDraw(mockRequest);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());
        verify(notificationService).sendNotificationForDraw(anyString());
    }

    @DisplayName("Test Offer Draw Game in Status Finished expected GameWrongDataException")
    @Test
    void offerDrawWithGameFinished() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGameWithBoardWithStatusFinished();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();
        String expectedMessage = "The game is already finished";

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        Exception exception = assertThrows(GameWrongDataException.class, () -> {
            gameCommonService.offerDraw(mockRequest);
        });

        String actualMessage = exception.getMessage();

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Test Draw answer false")
    @Test
    void drawAnswerFalse() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        gameCommonService.draw(mockRequest, false);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());
        verify(notificationService).sendNotificationForDrawResponse(anyBoolean(), anyString());

        assertEquals(gameRedis.getGameInfo().getStatus(), GameStatus.IN_PROCESS);
        assertNull(gameRedis.getGameInfo().getDetail());
    }

    @DisplayName("Test Draw answer true")
    @Test
    void drawAnswerTrue() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        Game game = ParameterFabric.createGame();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);
        given(gameInfoService.save(any(GameInfo.class))).willReturn(gameRedis.getGameInfo());
        given(gameMapper.gameRedisToGame(any(GameRedis.class))).willReturn(game);
        given(gameService.save(any(Game.class))).willReturn(game);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);


        gameCommonService.draw(mockRequest, true);

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());
        verify(notificationService).sendNotificationForDrawResponse(anyBoolean(), anyString());
        verify(gameInfoService).save(any(GameInfo.class));
        verify(gameMapper).gameRedisToGame(any(GameRedis.class));
        verify(gameService).save(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));
        verify(notificationService).sendNotificationForGameStatus(any(GameStatusNotify.class), anyString(), anyString());

        assertEquals(gameRedis.getGameInfo().getStatus(), GameStatus.FINISHED);
        assertEquals(gameRedis.getGameInfo().getDetail(), GameResult.DRAW);
    }

    @DisplayName("Test Draw With Game Finished Status")
    @Test
    void drawAnswerGameStatusError() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGameWithBoardWithStatusFinished();
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();
        String expectedMessage = "The game is already finished";

        given(jwtService.resolveToken(any(HttpServletRequest.class))).willReturn(gameRedis.getTokenForWhitePlayer());
        given(jwtService.getGameId(anyString())).willReturn(gameRedis.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);

        Exception exception = assertThrows(GameWrongDataException.class, () -> {
            gameCommonService.draw(mockRequest, true);
        });

        String actualMessage = exception.getMessage();

        verify(jwtService).resolveToken(any(HttpServletRequest.class));
        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Test Finish Game")
    @Test
    void finishGame() throws JsonProcessingException {
        GameRedis gameRedis = ParameterFabric.createRedisGame();
        Game game = ParameterFabric.createGame();

        given(jwtService.getGameId(anyString())).willReturn(game.getId());
        given(gameRedisService.get(anyInt())).willReturn(gameRedis);
        given(jwtService.getColor(anyString())).willReturn(gameRedis.getActive());
        given(gameInfoService.save(any(GameInfo.class))).willReturn(gameRedis.getGameInfo());
        given(gameMapper.gameRedisToGame(any(GameRedis.class))).willReturn(game);
        given(gameService.save(any(Game.class))).willReturn(game);
        given(gameRedisService.save(any(GameRedis.class))).willReturn(gameRedis);

        gameCommonService.finishGame(anyString());

        verify(jwtService).getGameId(anyString());
        verify(gameRedisService).get(anyInt());
        verify(jwtService).getColor(anyString());
        verify(gameInfoService).save(any(GameInfo.class));
        verify(notificationService).sendNotificationForGameStatus(any(GameStatusNotify.class), anyString(), anyString());
        verify(gameService).save(any(Game.class));
        verify(gameRedisService).save(any(GameRedis.class));

        assertEquals(gameRedis.getGameInfo().getStatus(), GameStatus.FINISHED);
        assertEquals(gameRedis.getGameInfo().getDetail(), GameResult.BLACK);
    }
}
