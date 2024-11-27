package com.chess.jnd.repository;

import com.chess.jnd.entity.CacheData;
import com.chess.jnd.entity.GameRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRedisRepository extends CrudRepository<CacheData<GameRedis>, Integer> {
}