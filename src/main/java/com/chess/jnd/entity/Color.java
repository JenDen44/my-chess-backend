package com.chess.jnd.entity;

import com.fasterxml.jackson.annotation.JsonValue;


public enum Color {
    WHITE("white"),
    BLACK("black");

    private final String color;

    Color(String color) {
        this.color = color;
    }

    @JsonValue
    public String getColor() {
        return color;
    }
}