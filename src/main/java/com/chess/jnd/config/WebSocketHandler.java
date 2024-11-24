package com.chess.jnd.config;


import com.chess.jnd.entity.Game;
import com.chess.jnd.notification.WebSocketSessionService;
import com.chess.jnd.service.GameService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionService sessionService;
    private final GameService gameService;
    private TimerTask task = null;

    public WebSocketHandler(WebSocketSessionService sessionService, GameService gameService) {
        this.sessionService = sessionService;
        this.gameService = gameService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String token = message.getPayload();
        Game game = gameService.findGameByToken(token);

        if (game == null) {
            session.close();

            return;
        }

        sessionService.addSession(session, token);

        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        var timer = new Timer();
        task = new TimerTask() {
            public void run() {
               try {
                   session.close();
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
            }
        };

        timer.schedule(task, 30000);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionService.removeSession(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        session.close();
    }
}