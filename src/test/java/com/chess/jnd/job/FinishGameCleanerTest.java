package com.chess.jnd.job;

import com.chess.jnd.event.RemoveChatEvent;
import com.chess.jnd.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinishGameCleanerTest {

    @InjectMocks
    private FinishGameCleaner finishGameCleaner;

    @Mock
    private GameService gameService;

    @Mock
    private ApplicationEventPublisher publisher;

    private List<String> tokens;

    @BeforeEach
    void setUp() {
        tokens = Arrays.asList("token1", "token2");
    }

    @DisplayName("Delete Finished Games And Publish Remove Chat Event")
    @Test
    void deleteFinishedGame() {
        when(gameService.removeAllFinishedGame()).thenReturn(tokens);

        finishGameCleaner.deleteFinishedGame();

        verify(gameService).removeAllFinishedGame();
        verify(publisher).publishEvent(any(RemoveChatEvent.class));
    }

    @DisplayName("Tokens Are Empty And Do Not Publish Remove Chat Event")
    @Test
    void deleteFinishedGame_whenTokensAreEmpty() {
        when(gameService.removeAllFinishedGame()).thenReturn(Arrays.asList());

        finishGameCleaner.deleteFinishedGame();

        verify(gameService).removeAllFinishedGame();
        verify(publisher, Mockito.never()).publishEvent(Mockito.any(RemoveChatEvent.class));
    }
}