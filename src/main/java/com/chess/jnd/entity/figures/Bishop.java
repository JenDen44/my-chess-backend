package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Bishop extends Figure {
    public Bishop(Color color, Cell cell) {
        super(FigureName.BISHOP, color, cell);
    }
}