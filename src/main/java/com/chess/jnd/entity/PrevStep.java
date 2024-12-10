package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.ShortFigureName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrevStep {

    private int fromX;

    private int fromY;

    private int toX;

    private int toY;
    private ShortFigureName fromFigure;

    private ShortFigureName toFigure;
}
