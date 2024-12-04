package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Bishop extends Figure {

    public Bishop(Color color, Cell cell) {
        super(FigureName.BISHOP, color, cell);
    }

    public boolean checkCorrectMove(Cell cell) {
        if (!super.checkCorrectMove(cell)) {
            return false;
        }

        return this.getCell().checkDiagonal(cell);
    }
}