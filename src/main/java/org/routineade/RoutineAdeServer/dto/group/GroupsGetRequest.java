package org.routineade.RoutineAdeServer.dto.group;

public record GroupsGetRequest(
        String groupCategory,
        Long groupCode,
        String keyword
) {
}
