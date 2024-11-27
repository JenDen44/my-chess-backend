package com.chess.jnd.controller;

import com.chess.jnd.entity.CreateGameRequest;
import com.chess.jnd.entity.GameInfoResponse;
import com.chess.jnd.entity.GameResponse;
import com.chess.jnd.entity.MoveRequest;
import com.chess.jnd.service.GameCommonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameController {

    private final GameCommonService gameService;

    @Autowired
    public GameController(GameCommonService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameResponse> createNewGame(@RequestBody CreateGameRequest gameRequest) throws JsonProcessingException {
       return ResponseEntity.ok(gameService.createGame(gameRequest));
    }

    @GetMapping("/game")
    public ResponseEntity<GameResponse> currentGame(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(gameService.getCurrentGame(request));
    }

    @PostMapping("/game/move")
    public ResponseEntity<GameInfoResponse> move(HttpServletRequest request,
                                                 @RequestBody MoveRequest moveRequest) throws JsonProcessingException {
        return ResponseEntity.ok(gameService.move(request, moveRequest));
    }

    @PostMapping("/game/give-up")
    public ResponseEntity<GameInfoResponse> giveUp(HttpServletRequest request) throws JsonProcessingException {
        return  ResponseEntity.ok(gameService.giveUp(request));
    }
}
