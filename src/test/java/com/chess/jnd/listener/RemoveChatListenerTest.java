package com.chess.jnd.listener;

import com.chess.jnd.client.ChatClient;
import com.chess.jnd.event.RemoveChatEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveChatListenerTest {

    @InjectMocks
    private RemoveChatListener removeChatListener;

    @Mock
    private  ChatClient chatClient;

    @DisplayName("test listen RemoveChatEvent and deleteChat")
    @Test
    void listen() {
        RemoveChatEvent mockRemoveChatEvent = Mockito.mock(RemoveChatEvent.class);

        Mockito.when(mockRemoveChatEvent.getTokens()).thenReturn(List.of("token1", "token2"));
        Mockito.when(chatClient.deleteChat(any(List.class))).thenReturn(true);

        removeChatListener.listen(mockRemoveChatEvent);

        verify(mockRemoveChatEvent).getTokens();
        verify(chatClient).deleteChat(any(List.class));
    }
}