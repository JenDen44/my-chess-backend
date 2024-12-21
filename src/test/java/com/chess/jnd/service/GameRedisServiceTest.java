package com.chess.jnd.service;

import com.chess.jnd.entity.CacheData;
import com.chess.jnd.entity.GameRedis;
import com.chess.jnd.repository.GameRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GameRedisServiceTest {

    @Mock
    GameRedisRepository gameRedisRepo;

    @InjectMocks
    GameRedisService gameRedisService;

    @DisplayName("Test Save Game Redis")
    @Test
    void save() {
        CacheData<GameRedis> cacheData = new CacheData<>();
        GameRedis gameRedis = new GameRedis();
        cacheData.setValue(gameRedis);

        given(gameRedisRepo.save(any(CacheData.class))).willReturn(cacheData);

        GameRedis gameRedisFromService = gameRedisService.save(new GameRedis());

        then(gameRedisRepo).should().save(any(CacheData.class));
        assertThat(gameRedisFromService).isNotNull();
    }

    @DisplayName("Test Get Game Redis By Id")
    @Test
    void get() {
        CacheData<GameRedis> cacheData = new CacheData<>();
        GameRedis gameRedis = new GameRedis();
        cacheData.setValue(gameRedis);

        given(gameRedisRepo.findById(anyInt())).willReturn(Optional.of(cacheData));

        GameRedis gameRedisFromService = gameRedisService.get(1);

        then(gameRedisRepo).should().findById(anyInt());
        assertThat(gameRedisFromService).isNotNull();
    }

    @DisplayName("Test Delete Game Redis By Id")
    @Test
    void delete() {
        gameRedisService.delete(1);

        then(gameRedisRepo).should().deleteById(anyInt());
    }
}