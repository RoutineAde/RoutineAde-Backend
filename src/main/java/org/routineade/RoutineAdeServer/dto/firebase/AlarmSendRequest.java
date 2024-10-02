package org.routineade.RoutineAdeServer.dto.firebase;

public record AlarmSendRequest(
        String deviceToken,
        String content
) {
}
