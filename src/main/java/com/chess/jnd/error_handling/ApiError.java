package com.chess.jnd.error_handling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private Integer code;
    private String message;
    private List<ErrorField> fields;

    public ApiError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}