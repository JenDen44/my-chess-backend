package com.chess.jnd.job;

import com.chess.jnd.event.RemoveChatEvent;
import com.chess.jnd.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FinishGameCleaner {

    private final GameService gameService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public FinishGameCleaner(GameService gameService, ApplicationEventPublisher publisher) {
        this.gameService = gameService;
        this.publisher = publisher;
    }

    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void deleteFinishedGame() {
        System.out.println("deleteFinishedGame started");
        List<String> tokens = gameService.removeAllFinishedGame();

        if (tokens != null && !tokens.isEmpty()) {
            System.out.println("tokens are not empty throw even to remove chats");
            publisher.publishEvent(new RemoveChatEvent(tokens));
        }
    }
}
