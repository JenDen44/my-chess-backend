package com.chess.jnd.entity;

public  class Figure {
    private FigureName name;
    private Color color;
    private Cell cell;

    public Figure(FigureName name, Color color, Cell cell) {
        this.name = name;
        this.color = color;
        this.cell = cell;
        this.cell.setFigure(this);
    }
}
