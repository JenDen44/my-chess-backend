package com.chess.jnd.notification;

import com.chess.jnd.entity.MoveNotify;

public class MoveNotification extends AbstractNotification<MoveNotify> {

    public MoveNotification(MoveNotify entity) {
        super(NotificationType.PLAYER_MOVED, entity);
    }
}