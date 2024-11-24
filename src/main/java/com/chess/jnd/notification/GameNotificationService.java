package com.chess.jnd.notification;

import com.chess.jnd.entity.GameStatusNotify;
import com.chess.jnd.entity.MoveNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameNotificationService {

    @Autowired
    private final WebSocketSessionService sessionService;

    public GameNotificationService(WebSocketSessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void sendNotificationForMove(MoveNotify moveNotify, String token) {
        sessionService.sendMessage(token, new MoveNotification(moveNotify));
    }

    public void sendNotificationForGameStatus(GameStatusNotify gameStatusNotify, String token) {
        sessionService.sendMessage(token, new GameStatusChangeNotification(gameStatusNotify));
    }
}