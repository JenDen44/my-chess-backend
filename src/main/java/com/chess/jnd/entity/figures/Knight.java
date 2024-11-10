package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Knight extends Figure {
    public Knight(Color color, Cell cell) {
        super(FigureName.KNIGHT, color, cell);
    }
}