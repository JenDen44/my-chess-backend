package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.Figure;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrevStep {
    private Cell fromCell;

    private Cell toCell;

    private Figure fromFigure;

    private Figure toFigure;

}
