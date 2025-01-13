package com.chess.jnd.entity;

import com.chess.jnd.entity.figures.ShortFigureName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GameRedis {
    private Integer id;

    private String tokenForWhitePlayer;

    private String tokenForBlackPlayer;

    private GameInfo gameInfo;

    private ShortFigureName[][] board;

    private Color active;

    private PassantCell passantCell;

    private PrevStep prevStep;

    private LocalDateTime date;

    private Integer timeForMove;

    @Override
    public String toString() {
        return "GameRedis{" +
                "id=" + id +
                ", gameInfo=" + gameInfo +
                ", board=" + Arrays.toString(board) +
                ", active=" + active +
                ", passantCell=" + passantCell +
                ", prevStep=" + prevStep +
                '}';
    }
}
