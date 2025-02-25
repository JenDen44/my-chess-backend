package com.chess.jnd.service;

import com.chess.jnd.ParameterFabric;
import com.chess.jnd.entity.Game;
import com.chess.jnd.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    GameRepository gameRepo;

    @InjectMocks
    GameService gameService;

    private  Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
        game.setTokenForBlackPlayer("token1");
        game.setTokenForWhitePlayer("token2");
    }
    @DisplayName("Test Save Game")
    @Test
    void save() {
        Game game = new Game();

        given(gameRepo.save(any(Game.class))).willReturn(game);

        Game gameFromService = gameService.save(new Game());

        verify(gameRepo).save(any(Game.class));

        assertThat(gameFromService).isNotNull();
    }

    @DisplayName("Test Get Game By ID")
    @Test
    void get() {
        Game game = new Game();

        given(gameRepo.findById(anyInt())).willReturn(Optional.of(game));

        Game gameFromService = gameService.get(1);

        verify(gameRepo).findById(anyInt());

        assertThat(gameFromService).isNotNull();
    }

    @DisplayName("Test Delete Game By ID")
    @Test
    void delete() {
        Game game = new Game();

        given(gameRepo.findById(anyInt())).willReturn(Optional.of(game));

        gameService.delete(1);

        verify(gameRepo).findById(anyInt());
        verify(gameRepo).deleteById(anyInt());
    }

    @DisplayName("Test Get all Finished Games")
    @Test
    void getAllFinishedGame() {
        Game game = new Game();
        game.setTokenForBlackPlayer("token1");
        game.setTokenForWhitePlayer("token2");
        List<Game> games = List.of(ParameterFabric.createGame(), game);
        given(gameRepo.findAllFinishedGame()).willReturn(games);

        List<Game> gamesFromService = gameService.getAllFinishedGame();

        verify(gameRepo).findAllFinishedGame();

        assertEquals(gamesFromService.size(), games.size());
        assertEquals(gamesFromService, games);
    }

    @DisplayName("Test Remove all Finished Games")
    @Test
    void removeAllFinishedGame() {
        List<Game> games = List.of(ParameterFabric.createGame(), game);
        given(gameRepo.findAllFinishedGame()).willReturn(games);

        List<String> tokensFinishedGames = gameService.removeAllFinishedGame();

        verify(gameRepo).findAllFinishedGame();
        verify(gameRepo).deleteAll(any(List.class));

        assertNotEquals(tokensFinishedGames.isEmpty(), true);
        assertEquals(tokensFinishedGames.size(), games.size());
    }

    @DisplayName("Test Remove All Finished Games When Games Are Empty")
    @Test
    void removeAllFinishedGame_whenGamesAreEmpty() {
        List<Game> games = new ArrayList<>();
        given(gameRepo.findAllFinishedGame()).willReturn(games);

        List<String> tokensFinishedGames = gameService.removeAllFinishedGame();

        verify(gameRepo).findAllFinishedGame();
        verify(gameRepo, Mockito.never()).deleteAll(any(List.class));

        assertEquals(tokensFinishedGames.isEmpty(), true);
    }
}