package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Queen extends Figure {
    public Queen(Color color, Cell cell) {
        super(FigureName.QUEEN, color, cell);
    }
}