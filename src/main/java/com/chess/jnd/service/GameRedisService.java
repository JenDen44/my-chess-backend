package com.chess.jnd.service;

import com.chess.jnd.entity.CacheData;
import com.chess.jnd.entity.Game;
import com.chess.jnd.repository.GameRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameRedisService {

    private final GameRedisRepository redisRepository;

    @Autowired
    public GameRedisService(GameRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public void save(Game game) {
        CacheData<Game> gameCacheData = new CacheData<>(game.getId(), game);
        redisRepository.save(gameCacheData);
    }

    public Game get(Integer gameId) {
        Optional<CacheData<Game>> gameCacheData = redisRepository.findById(gameId);

        if (gameCacheData.isPresent()) {
            return gameCacheData.get().getValue();
        }

        return null;
    }

    public void delete(Integer id) {
        redisRepository.deleteById(id);
    }
}
