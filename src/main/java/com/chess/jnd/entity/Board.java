package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.Figure;
import com.chess.jnd.entity.figures.FigureFactory;
import com.chess.jnd.entity.figures.FigureName;
import com.chess.jnd.entity.figures.ShortFigureName;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class Board {
    private ObjectMapper mapper = new ObjectMapper();

    private FigureFactory figureFactory;

    private Cell[][] cells = new Cell[8][8];

    private Cell passantCell;

    private PrevStep prevStep;

    public Board(ShortFigureName[][] shortNames, PassantCell cell, PrevStep prevStep) {
        this.figureFactory = new FigureFactory();
        this.init();
        this.addFigures(shortNames);

        this.prevStep = prevStep;

        if (cell != null) {
            this.setPassantCell(this.getCell(cell.getX(), cell.getY()));
        }
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
                    this.figureFactory.createByShortName(shortName, this.getCell(x, y));
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
                .fromX(fromCell.getX())
                .fromY(fromCell.getY())
                .toX(toCell.getX())
                .toY(toCell.getY())
                .fromFigure(fromCell.getFigure() != null ? fromCell.getFigure().getShortName() : null)
                .toFigure(toCell.getFigure() != null ? toCell.getFigure().getShortName() : null)
                .build();
    }

    public boolean checkIfMove(Figure figure, Cell toCell) {
        figure.move(toCell, false);

        boolean isCheck = isCheck(figure.getColor());

        this.goBack();

        return isCheck;
    }

    public  Figure findFigure(FigureName name, Color color) {
        for (int y = 0; y < this.cells.length; y++) {
            for (int x = 0; x < this.cells[y].length; x++) {
                Cell cell = this.getCell(x, y);
                Figure figureFromCell = cell.getFigure();

                if (figureFromCell != null && figureFromCell.getName() == name && figureFromCell.getColor() == color) {
                    return figureFromCell;
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
                Figure figureFromCell = cell.getFigure();

                if (
                    figureFromCell != null &&
                    figureFromCell.getColor() == oppositeColor &&
                    figureFromCell.checkCorrectMove(figure.getCell())
                ) {
                    return true;
                }
            }
        }

        return false;
    }

    public void goBack() {
        PrevStep prevStep = this.getPrevStep();

        if (prevStep == null) return;

        Cell fromCell = this.getCell(prevStep.getFromX(), prevStep.getFromY());
        Cell toCell = this.getCell(prevStep.getToX(), prevStep.getToY());
        ShortFigureName fromFigure = prevStep.getFromFigure();
        ShortFigureName toFigure = prevStep.getToFigure();

        if (fromCell != null && fromFigure != null) {
            this.figureFactory.createByShortName(fromFigure, fromCell);
        } else if (fromCell != null) {
            fromCell.setFigure(null);
        }

        if (toCell != null && toFigure != null) {
            this.figureFactory.createByShortName(toFigure, toCell);
        } else if (toCell != null) {
            toCell.setFigure(null);
        }
    }

    public boolean isCheck(Color color) {
        Figure king  = this.findFigure(FigureName.KING, color);

        return this.checkCanBeEaten(king);
    }

    public boolean canMove(Color color) {
        for (int y = 0; y < this.cells.length; y++) {
            for (int x = 0; x < this.cells[y].length; x++) {
                Cell figureCell = this.getCell(x, y);
                Figure figure = figureCell.getFigure();

                if (figure == null || !figure.getColor().equals(color)) continue;

                for (int cY = 0; cY < this.cells.length; cY++) {
                    for (int cX = 0; cX < this.cells[cY].length; cX++) {
                        Cell cell = this.getCell(cX, cY);

                        if (figureCell.getFigure().canMove(cell)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean isCheckMate(Color color) {
        if (!this.isCheck(color)) {
            return false;
        }

        return !this.canMove(color);
    }

    public boolean isDraw(Color color) {
        if (this.isCheck(color)) {
            return false;
        }

        return !this.canMove(color);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return "Board{" +
                "cells=" + mapper.writeValueAsString((this.getShortNames())) +
                "prevStep=" + mapper.writeValueAsString((this.getPrevStep())) +
                '}';
    }
}
