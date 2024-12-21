package com.chess.jnd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GameInfoResponse {

        private GameStatus status;

        private GameResult detail;
}
