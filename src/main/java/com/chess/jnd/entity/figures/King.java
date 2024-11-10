package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class King extends Figure {
    public King(Color color, Cell cell) {
        super(FigureName.KING, color, cell);
    }
}