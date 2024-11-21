package com.chess.jnd.service;

import com.chess.jnd.entity.CacheData;
import com.chess.jnd.entity.Game;
import com.chess.jnd.repository.GameRedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameRedisService {

    private final GameRedisRepository redisRepository;
    private final ObjectMapper mapper;

    @Autowired
    public GameRedisService(GameRedisRepository redisRepository, ObjectMapper mapper) {
        this.redisRepository = redisRepository;
        this.mapper = mapper;
    }

    public void save(Game game) throws JsonProcessingException {
        String gameAsJsonString = mapper.writeValueAsString(game);
        CacheData cacheData = new CacheData(game.getId(), gameAsJsonString);

        redisRepository.save(cacheData);
    }

    public Game get(Integer gameId) throws JsonProcessingException {
        Optional<CacheData> cacheData = redisRepository.findById(gameId);

        if (cacheData.isPresent()) {
            String gameString = cacheData.get().getValue();
            TypeReference<Game> mapType = new TypeReference<Game>(){};
            Game game = mapper.readValue(gameString, mapType);

            return game;
        }
        return null;
    }

    public void delete(Integer id) {
        redisRepository.deleteById(id);
    }
}
