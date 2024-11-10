package com.chess.jnd.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "game_info")
public class GameInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private GameStatus status;
    private GameResult detail;
    @OneToMany(targetEntity = GameInfo.class, mappedBy = "infoId", cascade = CascadeType.ALL)
    private List<Game> game;
}
