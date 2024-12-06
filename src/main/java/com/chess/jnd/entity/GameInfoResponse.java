package com.chess.jnd.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Game Info Response Model Information")
public class GameInfoResponse {

        @Schema(description = "Game status Enum type", example = "in_process / finished")
        private GameStatus status;

        @Schema(description = "Game result Enum type", example = "white / black / draw")
        private GameResult detail;
}
