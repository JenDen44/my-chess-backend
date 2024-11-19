package com.chess.jnd.controller;

import com.chess.jnd.entity.CreateGameRequest;
import com.chess.jnd.entity.GameResponse;
import com.chess.jnd.entity.MoveRequest;
import com.chess.jnd.service.GameInfoService;
import com.chess.jnd.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/{token}")
    public ResponseEntity<GameResponse> currentGame(@PathVariable String token) throws JsonProcessingException {
        return ResponseEntity.ok(gameService.getCurrentGame(UUID.fromString(token)));
    }

    @PostMapping("/{token}/move")
    public ResponseEntity<GameResponse> move(@PathVariable UUID token,
                                             @RequestBody MoveRequest moveRequest) {
        gameService.move(moveRequest);
        return new ResponseEntity<>(null);
    }

    @PostMapping("/{token}/give-up")
    public ResponseEntity<GameResponse> giveUp(@RequestParam UUID token) {
        gameService.giveUp(token);
        return new ResponseEntity<>(null);
    }
}
