package com.chess.jnd.repository;

import com.chess.jnd.entity.CacheData;
import com.chess.jnd.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRedisRepository extends CrudRepository<CacheData<Game>, Integer> {
}