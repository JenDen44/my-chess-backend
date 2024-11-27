package com.chess.jnd.entity;

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

    private Board board;

    private Color active;
}
