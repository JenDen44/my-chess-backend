package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.ShortFigureName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Game Response Model Information")
public class GameResponse {

    @Schema(description = "Token for white player", example = "set of random symbols")
    private String tokenForWhitePlayer;

    @Schema(description = "Token for black player", example = "set of random symbols")
    private String tokenForBlackPlayer;

    @Schema(description = "Game info (status, details)", example = "status: finished, in_process / result: white / black / draw")
    private GameInfoResponse info;

    @Schema(description = "Board matrix contains figures", example = "[['k'],['q']]")
    private ShortFigureName[][] board;

    @Schema(description = "Active color Enum type", example = "white / black")
    private Color active;

    @Schema(description = "Current color Enum type", example = "white / black")
    private Color currentColor;
}