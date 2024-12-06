package com.chess.jnd.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Game Request Model Information")
public class CreateGameRequest {

    @Schema(description = "Color Enum type", example = "white / black")
    private Color color;
}
