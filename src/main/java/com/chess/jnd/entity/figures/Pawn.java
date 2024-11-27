package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class Pawn extends Figure {
    private boolean isFirstStep = true;
    public Pawn(Color color, Cell cell) {
        super(FigureName.PAWN, color, cell);
    }

    public boolean canMove(Cell cell) {
        if (!super.canMove(cell)) {
            return false;
        }

        int currentDY = this.getColor() == Color.BLACK ? 1 : -1;
        int dx = cell.getX() - this.getCell().getX();
        int dy = cell.getY() - this.getCell().getY();

        if (cell.checkEnemy(this)) {
            return Math.abs(dx) == 1 && dy == currentDY;
        }

        if (Math.abs(dx) == 1 && dy == currentDY && this.getBoard().getPassantCell().compare(cell)) {
            return true;
        }

        if (dx != 0) {
            return false;
        }

        if (dy == currentDY) {
            return true;
        }

        if (
                this.isFirstStep &&
                        dy == 2 * currentDY &&
                        this.getBoard().getCell(this.getCell().getX(), this.getCell().getY() + currentDY).isEmpty()
        ) {
            return true;
        }

        return false;
    }

    public void move(Cell cell) {
        if (this.isFirstStep) {
            this.isFirstStep = false;
            int dx = cell.getX() - this.getCell().getX();
            int dy = cell.getY() - this.getCell().getY();
            int currentDY = this.getColor() == Color.BLACK ? 1 : -1;

            super.move(cell);

            if (dx == 0 && dy == 2 * currentDY) {
                this.getBoard().setPassantCell(this.getBoard().getCell(this.getCell().getX(), this.getCell().getY() - currentDY));
            }

            return;
        }

        if (cell.getY() == 0 || cell.getY() == 7) {
            super.move(cell);

            new Queen(this.getColor(), cell);

            return;
        }


        if (cell.compare(this.getBoard().getPassantCell())) {
            super.move(cell);

            int currentDY = this.getColor() == Color.BLACK ? -1 : 1;
            Cell figureCell = this.getBoard().getCell(cell.getX(), cell.getY() + currentDY);
            figureCell.setFigure(null);

            return;
        }

        super.move(cell);
    }

}