package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Pawn extends Figure {
    public Pawn(Color color, Cell cell) {
        super(FigureName.PAWN, color, cell);
    }
}