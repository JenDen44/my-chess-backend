package com.chess.jnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameRequest {

    private Color color;

    private Integer timeForMove;

    public CreateGameRequest(Color color) {
        this.color = color;
    }
}
