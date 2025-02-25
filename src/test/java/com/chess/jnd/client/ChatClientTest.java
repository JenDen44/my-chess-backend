package com.chess.jnd.client;

import com.chess.jnd.entity.ChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ChatClient.class)
class ChatClientTest {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    private List<String> tokens;

    @BeforeEach
    public void setUp() throws Exception {
        tokens = List.of("token1", "token2");

        String response =
                objectMapper.writeValueAsString(new ChatResponse(tokens));

        server.expect(requestTo("http://localhost:8088" ))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
    }

    @DisplayName("Create Chat")
    @Test
    void createChat() {
        ChatResponse actualResponse = chatClient.createChat(tokens);

        assertEquals(actualResponse.getTokens(), tokens);
    }

    @DisplayName("Delete Chat")
    @Test
    void deleteChat() {
        boolean removed = chatClient.deleteChat(tokens);

        assertEquals(removed, true);
    }
}