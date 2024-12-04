package com.chess.jnd.error_handling;

import java.util.List;

public class ApiErrorValidation extends ApiError {

    public ApiErrorValidation(List<ErrorField> fields) {
        super(422, "Validation Error", fields);
    }

    public ApiErrorValidation(String message) {
        super(422, message);
    }
}