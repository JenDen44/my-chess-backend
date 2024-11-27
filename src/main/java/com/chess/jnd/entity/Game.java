package com.chess.jnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "games")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "token_for_white_player")
    private String tokenForWhitePlayer;

    @Column(name = "token_for_black_player")
    private String tokenForBlackPlayer;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private GameInfo gameInfo;

    @Column(length = 1500)
    private String board;

    private Color active;

    public Game(String board) {
        this.gameInfo = new GameInfo(GameStatus.IN_PROCESS);
        this.board = board;
        this.active = Color.WHITE;
    }
}