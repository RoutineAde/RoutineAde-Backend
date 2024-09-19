package org.routineade.RoutineAdeServer.dto.firebase;

import org.routineade.RoutineAdeServer.domain.User;

public record FCMNotificationRequest(
        User user,
        String title,
        String body
) {
    public static FCMNotificationRequest of(User user, String title, String body) {
        return new FCMNotificationRequest(user, title, body);
    }
}
