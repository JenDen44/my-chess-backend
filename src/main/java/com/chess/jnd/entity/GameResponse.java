package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.ShortFigureName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResponse {

    private String tokenForWhitePlayer;

    private String tokenForBlackPlayer;

    private GameInfoResponse info;

    private ShortFigureName[][] board;

    private Color active;

    private Color currentColor;
}