package com.chess.jnd.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "game_info")
@NoArgsConstructor
public class GameInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private GameStatus status;

    private GameResult detail;

    public GameInfo(GameStatus status) {
        this.status = status;
    }
}
