package com.chess.jnd.notification;

public class DrawNotificationRequest extends AbstractNotification<Object> {

    public DrawNotificationRequest() {
        super(NotificationType.DRAW_REQUEST, null);
    }
}