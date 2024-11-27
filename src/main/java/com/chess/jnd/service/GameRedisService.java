package com.chess.jnd.service;

import com.chess.jnd.entity.CacheData;
import com.chess.jnd.entity.GameRedis;
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

    public GameRedis save(GameRedis game) {
        CacheData<GameRedis> gameCacheData = new CacheData<>(game.getId(), game);
        
        return redisRepository.save(gameCacheData).getValue();
    }

    public GameRedis get(Integer gameId) {
        Optional<CacheData<GameRedis>> gameCacheData = redisRepository.findById(gameId);

        if (!gameCacheData.isPresent()) {
          throw new RuntimeException("game is not found in Redis Repository");
        }

        return gameCacheData.get().getValue();
    }

    public void delete(Integer gameId) {
        redisRepository.deleteById(gameId);
    }
}
