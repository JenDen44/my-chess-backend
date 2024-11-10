package com.chess.jnd.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "game_info")
public class GameInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private GameStatus status;
    private GameResult detail;
}
