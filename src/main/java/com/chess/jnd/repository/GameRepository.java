package com.chess.jnd.repository;

import com.chess.jnd.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    @Query(
          value = """
          select g from Game g\s
          where g.tokenForBlackPlayer = :token or g.tokenForWhitePlayer = :token\s
        """
    )
    Game getGameByToken(UUID token);
}