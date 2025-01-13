package com.chess.jnd.service;

import com.chess.jnd.entity.GameInfo;
import com.chess.jnd.repository.GameInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class GameInfoServiceTest {

    @InjectMocks
    GameInfoService gameInfoService;
    @Mock
    GameInfoRepository gameInfoRepo;

    @DisplayName("Test Get Game Info By ID")
    @Test
    void get() {
        GameInfo gameInfo = new GameInfo();

        given(gameInfoRepo.findById(anyInt())).willReturn(Optional.of(gameInfo));

        GameInfo gameInfoFromService = gameInfoService.get(1);

        verify(gameInfoRepo).findById(anyInt());
        assertThat(gameInfoFromService).isNotNull();
    }

    @DisplayName("Test Save New Game Info")
    @Test
    void save() {
        GameInfo gameInfo = new GameInfo();

        given(gameInfoRepo.save(any(GameInfo.class))).willReturn(gameInfo);

        GameInfo gameInfoFromService = gameInfoService.save(new GameInfo());

        verify(gameInfoRepo).save(any(GameInfo.class));
        assertThat(gameInfoFromService).isNotNull();
    }

    @DisplayName("Test Delete Game Info By Id")
    @Test
    void deleteGameInfo() {
        GameInfo gameInfo = new GameInfo();

        given(gameInfoRepo.findById(anyInt())).willReturn(Optional.of(gameInfo));

        gameInfoService.deleteGameInfo(1);

        verify(gameInfoRepo).findById(anyInt());
        verify(gameInfoRepo).deleteById(anyInt());
    }
}