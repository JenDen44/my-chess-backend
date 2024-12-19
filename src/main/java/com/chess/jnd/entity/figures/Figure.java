package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Board;
import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Figure {
    private FigureName name;

    private Color color;

    private Cell cell;

    private Board board;

    public Figure(FigureName name, Color color, Cell cell) {
        this.name = name;
        this.color = color;
        this.cell = cell;
        this.cell.setFigure(this);
        this.board = this.cell.getBoard();
    }

     public ShortFigureName getShortName() {
        if (this.color == Color.WHITE) {
            switch (this.name) {
                case FigureName.KING:
                    return ShortFigureName.WHITE_KING;
                case FigureName.QUEEN:
                    return ShortFigureName.WHITE_QUEEN;
                case FigureName.BISHOP:
                    return ShortFigureName.WHITE_BISHOP;
                case FigureName.KNIGHT:
                    return ShortFigureName.WHITE_KNIGHT;
                case FigureName.ROOK:
                    return ShortFigureName.WHITE_ROOK;
                default:
                    return ShortFigureName.WHITE_PAWN;
            }
        }

        switch (this.name) {
            case FigureName.KING:
                return ShortFigureName.BLACK_KING;
            case FigureName.QUEEN:
                return ShortFigureName.BLACK_QUEEN;
            case FigureName.BISHOP:
                return ShortFigureName.BLACK_BISHOP;
            case FigureName.KNIGHT:
                return ShortFigureName.BLACK_KNIGHT;
            case FigureName.ROOK:
                return ShortFigureName.BLACK_ROOK;
            default:
                return ShortFigureName.BLACK_PAWN;
        }
    }

    public boolean canMove(Cell cell) {
        Figure figure = cell.getFigure();

        if (figure != null && figure.getName() == FigureName.KING) {
            return false;
        }

        if (!this.checkCorrectMove(cell)) {
            return false;
        }

        return !this.board.checkIfMove(this, cell);

    }

    public void move(Cell cell, boolean isResetPassant) {
        this.board.setPrevStep(this.cell, cell);
        this.cell.setFigure(null);
        this.cell = cell;
        this.cell.setFigure(this);

        if (isResetPassant) {
            this.board.setPassantCell(null);
        }
    }

    public boolean checkCorrectMove(Cell cell) {
        return cell.getFigure() == null || cell.getFigure().getColor() != this.color;
    }

    @Override
    public String toString() {
        return "Figure{" +
                "name=" + name +
                ", color=" + color +
                '}';
    }
}