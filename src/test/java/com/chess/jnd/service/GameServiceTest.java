package com.chess.jnd.service;

import com.chess.jnd.entity.Game;
import com.chess.jnd.repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    GameRepository gameRepo;

    @InjectMocks
    GameService gameService;

    @DisplayName("Test Save Game")
    @Test
    void save() {
        Game game = new Game();

        given(gameRepo.save(any(Game.class))).willReturn(game);

        Game gameFromService = gameService.save(new Game());

        then(gameRepo).should().save(any(Game.class));
        assertThat(gameFromService).isNotNull();
    }

    @DisplayName("Test Get Game By ID")
    @Test
    void get() {
        Game game = new Game();

        given(gameRepo.findById(anyInt())).willReturn(Optional.of(game));

        Game gameFromService = gameService.get(1);

        then(gameRepo).should().findById(anyInt());
        assertThat(gameFromService).isNotNull();
    }

    @DisplayName("Test Delete Game By ID")
    @Test
    void delete() {
        gameService.delete(1);

        then(gameRepo).should().deleteById(anyInt());
    }
}