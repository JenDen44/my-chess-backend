package com.chess.jnd.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GameStatus {
    IN_PROCESS("in_process"),
    FINISHED("finished");

    private final String status;

    GameStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
