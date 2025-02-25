package com.chess.jnd.client;

import com.chess.jnd.entity.ChatRequest;
import com.chess.jnd.entity.ChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class ChatClient {

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value(value = "${chat.service.url}")
    private String chatServiceUrl;

    public ChatClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ChatResponse createChat(List<String> tokens) {
        ChatRequest request = new ChatRequest(tokens);

        return restTemplate.postForObject(chatServiceUrl, request, ChatResponse.class);
    }

    public boolean deleteChat(List<String> tokens) {
        ChatRequest request = new ChatRequest(tokens);
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(request);

        ResponseEntity<Void> response = restTemplate.exchange(
                chatServiceUrl,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            log.debug("Chats were successfully deleted for related tokens");
            return true;
        } else {
            log.error("Failed to delete chats for related tokens");
            return false;
        }
    }
}
