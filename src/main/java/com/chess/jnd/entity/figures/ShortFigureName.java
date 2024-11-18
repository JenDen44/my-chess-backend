package com.chess.jnd.entity.figures;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ShortFigureName {
    WHITE_KING('K') ,
    WHITE_QUEEN('Q'),
    WHITE_BISHOP('B'),
    WHITE_KNIGHT('N'),
    WHITE_ROOK('R'),
    WHITE_PAWN('P'),
    BLACK_KING('k'),
    BLACK_QUEEN('q'),
    BLACK_BISHOP('b'),
    BLACK_KNIGHT('n'),
    BLACK_ROOK('r'),
    BLACK_PAWN('p');

    private final Character shortName;

    ShortFigureName(Character shortName) {
        this.shortName = shortName;
    }
    
    @JsonValue
    public Character getShortName() {
        return shortName;
    }
}