package com.chess.jnd.notification;

import com.chess.jnd.entity.GameStatusNotify;

public class GameStatusChangeNotification extends AbstractNotification<GameStatusNotify> {

    public GameStatusChangeNotification(GameStatusNotify gameStatusNotify) {
        super(NotificationType.STATUS_CHANGED, gameStatusNotify);
    }
}