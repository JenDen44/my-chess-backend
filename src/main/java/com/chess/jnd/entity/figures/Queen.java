package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Queen extends Figure {

    public Queen(Color color, Cell cell) {
        super(FigureName.QUEEN, color, cell);
    }

    public boolean canMove(Cell cell) {
        if (!super.canMove(cell)) {
            return false;
        }

        Cell targetCell = this.getCell();

        return targetCell.checkHorizontal(cell) || targetCell.checkVertical(cell) || targetCell.checkDiagonal(cell);
    }
}