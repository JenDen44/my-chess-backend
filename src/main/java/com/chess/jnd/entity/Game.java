package com.chess.jnd.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
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
    private GameInfo infoId;
    private String board;
}