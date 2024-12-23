package com.chess.jnd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveNotify {
    private int fromX;

    private int toX;

    private int fromY;

    private int toY;

    private Color activeColor;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
}
