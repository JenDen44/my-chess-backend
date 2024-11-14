package com.chess.jnd.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "games")
@NoArgsConstructor
public class Game {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "token_for_white_player")
    private UUID tokenForWhitePlayer;

    @Column(name = "token_for_black_player")
    private UUID tokenForBlackPlayer;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private GameInfo infoId;

    @Column(length = 1500)
    private String board;

    private Color active;

    public Game(String board) {
        this.infoId = new GameInfo(GameStatus.IN_PROCESS);
        this.board = board;
        this.active = Color.WHITE;
        this.tokenForWhitePlayer = UUID.randomUUID();
        this.tokenForBlackPlayer = UUID.randomUUID();
    }
}