package com.chess.jnd.entity;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {
    private Integer id;

    List<String> tokens;

    public ChatResponse(List<String> tokens) {
        this.tokens = tokens;
    }
}
