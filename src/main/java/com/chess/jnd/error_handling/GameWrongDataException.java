package com.chess.jnd.error_handling;

public class GameWrongDataException extends RuntimeException {
    public GameWrongDataException(String message) {
        super(message);
    }
}
