package com.chess.jnd.client;

import com.chess.jnd.entity.ChatCreateRequest;
import com.chess.jnd.entity.ChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ChatClient {

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value(value = "${chat.service.url}")
    private String createURL;

    public ChatClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ChatResponse createChat(List<String> tokens) {

        ChatCreateRequest request = new ChatCreateRequest(tokens);

        return restTemplate.postForObject(createURL, request, ChatResponse.class);
    }

}
