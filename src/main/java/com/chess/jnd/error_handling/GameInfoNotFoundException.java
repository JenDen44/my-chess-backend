package com.chess.jnd.error_handling;

public class GameInfoNotFoundException extends RuntimeException {
    public GameInfoNotFoundException(String message) {
        super(message);
    }
}
