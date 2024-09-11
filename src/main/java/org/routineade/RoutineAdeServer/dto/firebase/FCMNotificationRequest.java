package org.routineade.RoutineAdeServer.dto.firebase;

public record FCMNotificationRequest(
        Long userId,
        String title,
        String body
) {
}
