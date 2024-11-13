package com.chess.jnd.service;

import com.chess.jnd.entity.GameInfo;
import com.chess.jnd.repository.GameInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class GameInfoService {
    private final GameInfoRepository gameInfoRepository;

    @Autowired
    public GameInfoService(GameInfoRepository gameInfoRepository) {
        this.gameInfoRepository = gameInfoRepository;
    }

    public GameInfo findGameInfoById(Integer id) {
        var gameInfo = gameInfoRepository.findById(id)
                .orElseThrow (() -> new RuntimeException("GameInfo with id " + id + " is not found in DB"));

        return gameInfo;
    }

    public GameInfo saveGameInfo(GameInfo gameInfo) {
        return gameInfoRepository.save(gameInfo);
    }

    public GameInfo updateGameInfo(GameInfo gameInfo, Integer id) {

        var gameInfoFromDb = gameInfoRepository.findById(id)
                .orElseThrow (() -> new RuntimeException("GameInfo with id " + id + " is not found in DB"));

        gameInfoFromDb.setDetail(gameInfo.getDetail());
        gameInfoFromDb.setStatus(gameInfo.getStatus());
        gameInfoRepository.save(gameInfoFromDb);

        return gameInfoFromDb;
    }

    public void deleteGameInfo(Integer id) {
        gameInfoRepository.findById(id)
                .orElseThrow (() -> new RuntimeException("GameInfo with id " + id + " is not found in DB"));

        gameInfoRepository.deleteById(id);
    }
}
