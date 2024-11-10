package com.chess.jnd.entity;

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
}
