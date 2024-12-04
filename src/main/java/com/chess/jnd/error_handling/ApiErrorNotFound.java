package com.chess.jnd.error_handling;

public class ApiErrorNotFound extends ApiError {

    public ApiErrorNotFound(String message) {
        super(404, message);
    }
}