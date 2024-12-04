package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class King extends Figure {

    public King(Color color, Cell cell) {
        super(FigureName.KING, color, cell);
    }

    public boolean checkCorrectMove(Cell cell) {
        if (!super.checkCorrectMove(cell)) {
            return false;
        }

        Cell targetCell = this.getCell();
        int dx = Math.abs(targetCell.getX() - cell.getX());
        int dy = Math.abs(targetCell.getY() - cell.getY());

        return dx <= 1 && dy <= 1;
    }
}