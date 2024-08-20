package org.routineade.RoutineAdeServer.dto.user;

public record RoutineCompletionInfo(
        Integer day,
        Integer level
) {
    public static RoutineCompletionInfo of(Integer day, Integer level) {
        return new RoutineCompletionInfo(day, level);
    }
}
