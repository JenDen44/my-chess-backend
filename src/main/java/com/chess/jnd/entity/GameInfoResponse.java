package com.chess.jnd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameInfoResponse {

        private GameStatus status;

        private GameResult detail;

        public GameInfoResponse(GameStatus status) {
                this.status = status;
        }
}
