package com.chess.jnd.notification;

public class DrawNotificationResponse extends AbstractNotification<Object> {
    public DrawNotificationResponse(boolean answer) {
        super(NotificationType.DRAW_RESPONSE, answer);
    }
}