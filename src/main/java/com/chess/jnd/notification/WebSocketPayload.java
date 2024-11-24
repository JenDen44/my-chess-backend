package com.chess.jnd.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import org.springframework.web.socket.TextMessage;

@Data
public class WebSocketPayload<TData extends Object> {

    private static final ObjectMapper mapper = JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .build();

    private TData data;

    public WebSocketPayload(TData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        try {
            return mapper.writer().writeValueAsString(this.data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return "Invalid WebSocketPayload";
        }
    }

    public TextMessage toTextMessage() {
        return new TextMessage(toString());
    }
}