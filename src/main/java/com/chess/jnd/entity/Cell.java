package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.Figure;
import lombok.Data;

@Data
public class Cell {
    private int x;

    private int y;

    private Color color;

    private Figure figure;

    public Cell(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
