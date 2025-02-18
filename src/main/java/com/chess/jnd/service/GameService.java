package com.chess.jnd.service;

import com.chess.jnd.entity.Game;
import com.chess.jnd.error_handling.GameNotFoundException;
import com.chess.jnd.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public Game get(Integer gameId) {
        Optional<Game> game = gameRepository.findById(gameId);

        if (!game.isPresent()) {
         log.error("Game is not found in DB by id {}", gameId);
          throw new GameNotFoundException("Game is not found in DB by id " + gameId);
        }

        return game.get();
    }

    public void delete(Integer gameId) {
        get(gameId);
        gameRepository.deleteById(gameId);
    }

    public List<Game> getAllFinishedGame() {
        return gameRepository.findAllFinishedGame("2");
    }

    public List<String> removeAllFinishedGame() {
        List<Game> finishedGames = getAllFinishedGame();
        List<String> tokensFinishedGames = new ArrayList<>();

        if (finishedGames.isEmpty()) {
            log.info("No finished game are found in DB to remove");
        } else {
            finishedGames.stream().forEach(game -> tokensFinishedGames.add(game.getTokenForBlackPlayer()));
            gameRepository.deleteAll(finishedGames);
        }

        return tokensFinishedGames;
    }
}
