package com.chess.jnd.entity.figures;

import lombok.Getter;

@Getter
public enum FigureName {
    KING("king"),
    QUEEN("queen"),
    BISHOP("bishop"),
    KNIGHT("knight"),
    ROOK("rook"),
    PAWN("pawn");

    private final String name;

    FigureName(String name) {
        this.name = name;
    }
}
