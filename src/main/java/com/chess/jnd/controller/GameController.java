package com.chess.jnd.controller;

import com.chess.jnd.entity.*;
import com.chess.jnd.error_handling.ApiErrorNotFound;
import com.chess.jnd.error_handling.ApiErrorValidation;
import com.chess.jnd.service.GameCommonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Game controller")
public class GameController {

    private final GameCommonService gameService;

    @Autowired
    public GameController(GameCommonService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Create new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game is created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class)) }),
            @ApiResponse(responseCode = "422", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorValidation.class))) })
    @PostMapping("/start")
    @ResponseStatus(HttpStatus.OK)
    public GameResponse createNewGame(@RequestBody CreateGameRequest gameRequest) throws JsonProcessingException {
       return gameService.createGame(gameRequest);
    }

    @Operation(summary = "Get current game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the current Game",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameResponse.class)) }),
            @ApiResponse(responseCode = "422", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorNotFound.class))) })
    @GetMapping("/game")
    @ResponseStatus(HttpStatus.OK)
    public GameResponse currentGame(HttpServletRequest request) throws JsonProcessingException {
        return gameService.getCurrentGame(request);
    }

    @Operation(summary = "Perform Move")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Move was performed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameInfoResponse.class)) }),
            @ApiResponse(responseCode = "422", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorValidation.class))),
            @ApiResponse(responseCode = "404", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorNotFound.class))) })
    @PostMapping("/game/move")
    @ResponseStatus(HttpStatus.OK)
    public GameInfoResponse move(HttpServletRequest request,
                                                 @RequestBody MoveRequest moveRequest) throws JsonProcessingException {
        return gameService.move(request, moveRequest);
    }

    @Operation(summary = "Give Up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The player gave up, Game is over",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameInfoResponse.class)) }),
            @ApiResponse(responseCode = "422", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorValidation.class))),
            @ApiResponse(responseCode = "404", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorNotFound.class))) })
    @PostMapping("/game/give-up")
    @ResponseStatus(HttpStatus.OK)
    public GameInfoResponse giveUp(HttpServletRequest request) throws JsonProcessingException {
        return gameService.giveUp(request);
    }

    @Operation(summary = "Offer Draw")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The player offered draw",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorValidation.class))),
            @ApiResponse(responseCode = "404", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorNotFound.class))) })
    @PostMapping("/game/draw")
    @ResponseStatus(HttpStatus.OK)
    public void offerDraw(HttpServletRequest request) throws JsonProcessingException {
        gameService.offerDraw(request);
    }

    @Operation(summary = "Draw answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The player sent answer for draw",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorValidation.class))),
            @ApiResponse(responseCode = "404", description = "Validation Error",
                    content = @Content (mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorNotFound.class))) })
    @PostMapping("/game/draw/{answer}")
    @ResponseStatus(HttpStatus.OK)
    public void draw(HttpServletRequest request, @PathVariable boolean answer) throws JsonProcessingException {
        gameService.draw(request, answer);
    }
}
