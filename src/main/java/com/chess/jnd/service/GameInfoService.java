package com.chess.jnd.service;

import com.chess.jnd.entity.GameInfo;
import com.chess.jnd.error_handling.GameInfoNotFoundException;
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

    public GameInfo get(Integer infoId) {
        var gameInfo = gameInfoRepository.findById(infoId).orElseThrow
                (() -> new GameInfoNotFoundException("GameInfo with id is not found in DB by id " + infoId));

        return gameInfo;
    }

    public GameInfo save(GameInfo gameInfo) {
        return gameInfoRepository.save(gameInfo);
    }

    public void deleteGameInfo(Integer id) {
       get(id);
        gameInfoRepository.deleteById(id);
    }
}
