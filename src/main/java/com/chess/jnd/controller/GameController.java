package com.chess.jnd.controller;

import com.chess.jnd.entity.CreateGameRequest;
import com.chess.jnd.entity.GameResponse;
import com.chess.jnd.entity.MoveRequest;
import com.chess.jnd.service.GameInfoService;
import com.chess.jnd.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameController {

    private final GameService gameService;
    private final GameInfoService gameInfoService;

    @Autowired
    public GameController(GameService gameService, GameInfoService gameInfoService) {
        this.gameService = gameService;
        this.gameInfoService = gameInfoService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameResponse> createNewGame(@RequestBody CreateGameRequest gameRequest) throws JsonProcessingException {
       return ResponseEntity.ok(gameService.createNewGame(gameRequest));
    }

    @GetMapping("/game")
    public ResponseEntity<GameResponse> currentGame(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(gameService.getCurrentGame(request));
    }

    @PostMapping("/game/move")
    public ResponseEntity<GameResponse> move(HttpServletRequest request,
                                             @RequestBody MoveRequest moveRequest) {
        gameService.move(request, moveRequest);
        return new ResponseEntity<>(null);
    }

    @PostMapping("/game/give-up")
    public ResponseEntity<GameResponse> giveUp(HttpServletRequest request) {
        gameService.giveUp(request);
        return new ResponseEntity<>(null);
    }
}
