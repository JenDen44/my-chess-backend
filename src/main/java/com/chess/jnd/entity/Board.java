package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.FigureFactory;
import com.chess.jnd.entity.figures.ShortFigureName;

public class Board {
    private FigureFactory figureFactory;
    private Cell[][] cells;

    Board(ShortFigureName[][] shortNames) {
        this.figureFactory = new FigureFactory();
        this.init();
        this.addFigures(shortNames);
    }

    private void init() {
        Cell cells [][] = new Cell[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Color color = (x + y) % 2 == 1 ? Color.BLACK : Color.WHITE;
                Cell cell = new Cell(x, y, color);
                cells[y][x] = cell;
            }
        }

        this.cells = cells;
    }

    private void addFigures(ShortFigureName[][] shortNames) {
        for (int y = 0; y < shortNames.length; y++) {
            for (int x = 0; x < shortNames[y].length; x++) {
                var shortName = shortNames[y][x];

                if (shortName != null) this.figureFactory.createByShortName(shortName, this.getCell(x, y));
            }
        }
    }

    private Cell getCell(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) throw new RuntimeException("Не доступные координаты x =" + x + " and " + " y=" + y);

        return this.cells[y][x];
    }
}
