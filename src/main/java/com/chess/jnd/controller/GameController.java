package com.chess.jnd.controller;

import com.chess.jnd.entity.CreateGameRequest;
import com.chess.jnd.entity.GameResponse;
import com.chess.jnd.service.GameInfoService;
import com.chess.jnd.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
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
    public ResponseEntity<GameResponse> currentGame(@RequestParam UUID token) throws JsonProcessingException {
        return ResponseEntity.ok(gameService.getCurrentGame(token));
    }
}
