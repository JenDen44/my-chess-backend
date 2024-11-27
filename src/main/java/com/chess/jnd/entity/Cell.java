package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.Figure;
import lombok.Data;

@Data
public class Cell {
    private int x;

    private int y;

    private Color color;

    private Figure figure;

    private Board board;

    public Cell(int x, int y, Color color, Board board) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.board = board;
    }

    public boolean compare(Cell cell) {
        return this.getX() == cell.getX() && this.getY() == cell.getY();
    }

    public boolean isEmpty() {
        return this.getFigure() == null;
    }


    public boolean checkHorizontal(Cell cell) {
        if (this.getY() != cell.getY()) return false;

        int minX = Math.min(this.getX(), cell.getX());
        int maxX = Math.max(this.getX(), cell.getX());

        for (int x = minX + 1; x < maxX; x++) {
            if (!this.getBoard().getCell(x, this.getY()).isEmpty()) return false;
        }
        return true;
    }

    public boolean checkVertical(Cell cell) {
        if (this.getX() != cell.getX()) return false;

        int minY = Math.min(this.getY(), cell.getY());
        int maxY = Math.max(this.getY(), cell.getY());

        for (int y = minY + 1; y < maxY; y++) {
            if (!this.getBoard().getCell(this.x, y).isEmpty()) return false;
        }
        return true;
    }

    public boolean checkDiagonal(Cell cell) {
        int difX = cell.getX() - this.getX();
        int difY = cell.getY() - this.getY();

        if (Math.abs(difX) != Math.abs(difY)) return false;

        int dx = (int) Math.signum(difX);
        int dy = (int) Math.signum(difY);

        for (int i = 1; i < Math.abs(difX); i++) {
            if (!this.getBoard().getCell(this.getX() + dx * i, this.getY() + dy * i).isEmpty()) return false;
        }
        return true;
    }

    public boolean checkEnemy(Figure figure) {
        return this.figure.getColor() != figure.getColor();
    }
}

