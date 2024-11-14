package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.ShortFigureName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResponse {
    private UUID tokenForWhitePlayer;

    private UUID tokenForBlackPlayer;

    private GameInfo info;

    private ShortFigureName[][] board;

    private Color active;

    private Color currentColor;
}