package com.chess.jnd.error_handling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ApiErrorNotFound> gameExceptionHandler(GameNotFoundException ex) {
        final var apiErrorNotFound = new ApiErrorNotFound(ex.getLocalizedMessage());

        return new ResponseEntity(apiErrorNotFound, new HttpHeaders(), apiErrorNotFound.getCode());
    }

    @ExceptionHandler(GameInfoNotFoundException.class)
    public ResponseEntity<ApiErrorNotFound> gameInfoExceptionHandler(GameInfoNotFoundException ex) {
        final var apiErrorNotFound = new ApiErrorNotFound(ex.getLocalizedMessage());

        return new ResponseEntity(apiErrorNotFound, new HttpHeaders(), apiErrorNotFound.getCode());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ApiErrorNotFound> tokenExceptionHandler(TokenNotFoundException ex) {
        final var apiErrorNotFound = new ApiErrorNotFound(ex.getLocalizedMessage());

        return new ResponseEntity(apiErrorNotFound, new HttpHeaders(), apiErrorNotFound.getCode());
    }

    @ExceptionHandler(GameWrongDataException.class)
    public ResponseEntity<ApiErrorNotFound> gameValidationExceptionHandler(GameWrongDataException ex) {
        final var apiErrorValidation = new ApiErrorValidation(ex.getLocalizedMessage());

        return new ResponseEntity(apiErrorValidation, new HttpHeaders(), apiErrorValidation.getCode());
    }
}
