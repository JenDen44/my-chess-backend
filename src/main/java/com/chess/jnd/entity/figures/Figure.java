package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;
import lombok.Data;

@Data
public class Figure {
    private FigureName name;

    private Color color;

    private Cell cell;

    public Figure(FigureName name, Color color, Cell cell) {
        this.name = name;
        this.color = color;
        this.cell = cell;
        this.cell.setFigure(this);
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

        if (figure == null) return true;

        return figure.getColor() != this.color && figure.getName() != FigureName.KING;
    }

    public void move(Cell cell) {
        if (this.canMove(cell)) {
            this.cell.setFigure(null);
            this.cell = cell;
            this.cell.setFigure(this);
        }
    }
}