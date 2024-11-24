package com.chess.jnd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveNotify {
    private int fromX;

    private int toX;

    private int fromY;

    private int toY;

    private Color activeColor;
}
