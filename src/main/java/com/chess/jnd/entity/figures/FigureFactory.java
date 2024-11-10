package com.chess.jnd.entity.figures;

import com.chess.jnd.entity.Cell;
import com.chess.jnd.entity.Color;

public class FigureFactory {

    public Figure createFigure(FigureName name, Color color, Cell cell) {
        switch (name) {
            case FigureName.KING:
                return new King(color, cell);
            case FigureName.QUEEN:
                return new Queen(color, cell);
            case FigureName.BISHOP:
                return new Bishop(color, cell);
            case FigureName.KNIGHT:
                return new Knight(color, cell);
            case FigureName.ROOK:
                return new Rook(color, cell);
            default:
                return new Pawn(color, cell);
        }
    }

    public Figure createByShortName(ShortFigureName shortName, Cell cell) {
        var name = this.getName(shortName);
        var color = this.getColor(shortName);

        return this.createFigure(name, color, cell);
    }

    private FigureName getName(ShortFigureName shortName) {
        switch (shortName) {
            case ShortFigureName.WHITE_KING:
            case ShortFigureName.BLACK_KING:
                return FigureName.KING;
            case ShortFigureName.WHITE_QUEEN:
            case ShortFigureName.BLACK_QUEEN:
                return FigureName.QUEEN;
            case ShortFigureName.WHITE_BISHOP:
            case ShortFigureName.BLACK_BISHOP:
                return FigureName.BISHOP;
            case ShortFigureName.WHITE_KNIGHT:
            case ShortFigureName.BLACK_KNIGHT:
                return FigureName.KNIGHT;
            case ShortFigureName.WHITE_ROOK:
            case ShortFigureName.BLACK_ROOK:
                return FigureName.ROOK;
            default:
                return FigureName.PAWN;
        }
    }

    private Color getColor(ShortFigureName shortName) {
        switch (shortName) {
            case ShortFigureName.WHITE_KING:
            case ShortFigureName.WHITE_QUEEN:
            case ShortFigureName.WHITE_BISHOP:
            case ShortFigureName.WHITE_KNIGHT:
            case ShortFigureName.WHITE_ROOK:
            case ShortFigureName.WHITE_PAWN:
                return Color.WHITE;
            default:
                return Color.BLACK;
        }
    }
}