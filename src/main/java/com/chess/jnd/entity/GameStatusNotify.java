package com.chess.jnd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStatusNotify {

    private GameStatus status;

    private GameResult detail;
}
