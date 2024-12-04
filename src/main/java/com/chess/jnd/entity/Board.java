package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.Figure;
import com.chess.jnd.entity.figures.FigureFactory;
import com.chess.jnd.entity.figures.FigureName;
import com.chess.jnd.entity.figures.ShortFigureName;
import lombok.Data;

@Data
public class Board {
    private FigureFactory figureFactory;
    private Cell[][] cells = new Cell[8][8];
    private Cell passantCell;
    private PrevStep prevStep;

    public Board(ShortFigureName[][] shortNames) {
        this.figureFactory = new FigureFactory();
        this.init();
        this.addFigures(shortNames);
    }

    private void init() {
        Cell cells [][] = new Cell[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Color color = (x + y) % 2 == 1 ? Color.BLACK : Color.WHITE;
                Cell cell = new Cell(x, y, color, this);
                cells[y][x] = cell;
            }
        }

        this.cells = cells;
    }

    private void addFigures(ShortFigureName[][] shortNames) {
        for (int y = 0; y < shortNames.length; y++) {
            for (int x = 0; x < shortNames[y].length; x++) {
                var shortName = shortNames[y][x];

                if (shortName != null) {
                    Figure newFigure = this.figureFactory.createByShortName(shortName, this.getCell(x, y));
                    newFigure.setBoard(this);
                }
            }
        }
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) throw new RuntimeException("Не доступные координаты x =" + x + " and " + " y=" + y);

        if (this.cells[y][x] == null) throw new RuntimeException("Доска не иницилезирована");

        return this.cells[y][x];
    }

    public ShortFigureName[][] getShortNames() {
        ShortFigureName[][] shortFigureNames = new ShortFigureName[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Cell cell = getCell(x,y);
                Figure figure = cell.getFigure();
                if (figure != null) {
                    shortFigureNames[y][x] = figure.getShortName();
                }
            }
        }

        return shortFigureNames;
    }

    public void setPrevStep(Cell fromCell, Cell toCell) {
        this.prevStep = PrevStep.builder()
                .fromCell(fromCell)
                .toCell(toCell)
                .fromFigure(fromCell.getFigure())
                .toFigure(toCell.getFigure())
                .build();
    }

    public boolean checkIfMove(Figure figure, Cell toCell) {
        figure.move(toCell, false);

        Figure king = this.findFigure(FigureName.KING, figure.getColor());
        boolean canBeEaten = this.checkCanBeEaten(king);

        this.goBack();

        return canBeEaten;
    }

    public  Figure findFigure(FigureName name, Color color) {
        for (int y = 0; y < this.cells.length; y++) {
            for (int x = 0; x < this.cells[y].length; x++) {
                Cell cell = this.getCell(x, y);

                if (cell.getFigure().getName() == name && cell.getFigure().getColor() == color) {
                    return cell.getFigure();
                }
            }
        }

        return null;
    }

    public boolean checkCanBeEaten(Figure figure) {
        if (figure == null) {
            return false;
        }

        Color oppositeColor = figure.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE;

        for (int y = 0; y < this.cells.length; y++) {
            for (int x = 0; x < this.cells[y].length; x++) {
                Cell cell = this.getCell(x, y);

                if (cell.getFigure().getColor() == oppositeColor && cell.getFigure().checkCorrectMove(figure.getCell())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void goBack() {
        PrevStep prevStep = this.getPrevStep();
        if (prevStep != null) {
            prevStep.getFromCell().setFigure(prevStep.getFromFigure());
            prevStep.getToCell().setFigure(prevStep.getToFigure());
        }
    }
}
