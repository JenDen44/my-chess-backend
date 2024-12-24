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
        sessionService.sendMessage(new MoveNotification(moveNotify), token);
    }

    public void sendNotificationForGameStatus(GameStatusNotify gameStatusNotify, String... tokens) {
        sessionService.sendMessage(new GameStatusChangeNotification(gameStatusNotify), tokens);
    }

    public void sendNotificationForDraw(String token) {
        sessionService.sendMessage(new DrawNotificationRequest(), token);
    }

    public void sendNotificationForDrawResponse(boolean answer, String token) {
        sessionService.sendMessage(new DrawNotificationResponse(answer), token);
    }
}