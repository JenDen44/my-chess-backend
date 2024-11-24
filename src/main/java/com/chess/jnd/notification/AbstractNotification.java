package com.chess.jnd.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbstractNotification<Entity> {

    private NotificationType type;
    private Entity entity;
}