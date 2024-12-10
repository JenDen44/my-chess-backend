package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.ShortFigureName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GameRedis {
    private Integer id;

    private String tokenForWhitePlayer;

    private String tokenForBlackPlayer;

    private GameInfo gameInfo;

    private ShortFigureName[][] board;

    private Color active;

    private PassantCell passantCell;

    private PrevStep prevStep;

}
