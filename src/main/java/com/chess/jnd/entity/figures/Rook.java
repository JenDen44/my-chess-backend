package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Rook extends Figure {
    public Rook(Color color, Cell cell) {
        super(FigureName.ROOK, color, cell);
    }
}