package com.chess.jnd.service;

import com.chess.jnd.entity.*;
import com.chess.jnd.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
          throw new RuntimeException("game is not found in DB");
        }

        return game.get();
    }

    public void delete(Integer gameId) {
        gameRepository.deleteById(gameId);
    }
}
