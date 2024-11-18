package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Knight extends Figure {

    public Knight(Color color, Cell cell) {
        super(FigureName.KNIGHT, color, cell);
    }

    public boolean canMove(Cell cell) {
        if (!super.canMove(cell)) {
            return false;
        }

        Cell targetCell = this.getCell();
        int dx = Math.abs(targetCell.getX() - cell.getX());
        int dy = Math.abs(targetCell.getY() - cell.getY());

        return dx == 1 && dy == 2 || dx == 2 && dy == 1;
    }
}