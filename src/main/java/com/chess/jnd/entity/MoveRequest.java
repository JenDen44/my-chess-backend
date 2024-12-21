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

    int fromX;

    int toX;

    int fromY;

    int toY;
}