package org.routineade.RoutineAdeServer.dto.routine;

import org.routineade.RoutineAdeServer.domain.Routine;

public record GroupRoutineInfo(
        Long routineId,
        String routineTitle,
        Boolean isCompletion
) {
    public static GroupRoutineInfo of(Routine routine, Boolean isCompletion) {
        return new GroupRoutineInfo(routine.getRoutineId(), routine.getRoutineTitle(), isCompletion);
    }
}
