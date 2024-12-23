package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.ShortFigureName;
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
public class GameResponse {

    private String tokenForWhitePlayer;

    private String tokenForBlackPlayer;

    private GameInfoResponse info;

    private ShortFigureName[][] board;

    private Color active;

    private Color currentColor;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
}