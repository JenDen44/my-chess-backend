package com.chess.jnd.repository;

import com.chess.jnd.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    @Query(value =
            """
            SELECT game.* FROM games game
            WHERE finish_date is not null
            AND finish_date + INTERVAL '2 hour' <= NOW()
            """
            , nativeQuery = true)
    List<Game> findAllFinishedGame(@Param("hours") String hours);
}