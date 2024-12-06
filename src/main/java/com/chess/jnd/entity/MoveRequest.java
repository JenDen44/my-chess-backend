package com.chess.jnd.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Move Request Model Information")
public class MoveRequest {

    @Schema(description = "Figure coordinate from X", example = "integer value : 1,3,4")
    int fromX;

    @Schema(description = "Figure coordinate to X", example = "integer value : 1,3,4")
    int toX;

    @Schema(description = "Figure coordinate from Y", example = "integer value : 1,3,4")
    int fromY;

    @Schema(description = "Figure coordinate to Y", example = "integer value : 1,3,4")
    int toY;
}