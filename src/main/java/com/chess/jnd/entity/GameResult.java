package com.chess.jnd.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GameResult {
    WHITE("white"),
    BLACK("black"),
    DRAW("draw");

    private final String result;

    GameResult(String result) {
        this.result = result;
    }

    @JsonValue
    public String getResult() {
        return result;
    }
}
