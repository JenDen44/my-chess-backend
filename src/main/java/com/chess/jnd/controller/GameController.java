package com.chess.jnd.controller;

import com.chess.jnd.entity.CreateGameRequest;
import com.chess.jnd.entity.GameInfoResponse;
import com.chess.jnd.entity.GameResponse;
import com.chess.jnd.entity.MoveRequest;
import com.chess.jnd.service.GameCommonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Game controller")
public class GameController {

    private final GameCommonService gameService;

    @Autowired
    public GameController(GameCommonService gameService) {
        this.gameService = gameService;
    }

    @Operation(
            description = "Endpoint to create new game",
            summary = "If you need to start new game, please use this endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),

                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "422"
                    )
            }
    )
    @PostMapping("/start")
    public ResponseEntity<GameResponse> createNewGame(@RequestBody CreateGameRequest gameRequest) throws JsonProcessingException {
       return ResponseEntity.ok(gameService.createGame(gameRequest));
    }

    @Operation(
            description = "Endpoint to get current game",
            summary = "If you need to to get current game, please use this endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),

                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "422"
                    ),

                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/game")
    public ResponseEntity<GameResponse> currentGame(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(gameService.getCurrentGame(request));
    }

    @Operation(
            description = "Endpoint to perform move on board",
            summary = "If you need to perform move on board, please use this endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),

                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "422"
                    )
            }
    )
    @PostMapping("/game/move")
    public ResponseEntity<GameInfoResponse> move(HttpServletRequest request,
                                                 @RequestBody MoveRequest moveRequest) throws JsonProcessingException {
        return ResponseEntity.ok(gameService.move(request, moveRequest));
    }

    @Operation(
            description = "Endpoint to give up",
            summary = "If you need give up, please use this endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),

                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "422"
                    ),

                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404"
                    )
            }
    )
    @PostMapping("/game/give-up")
    public ResponseEntity<GameInfoResponse> giveUp(HttpServletRequest request) throws JsonProcessingException {
        return  ResponseEntity.ok(gameService.giveUp(request));
    }
}
