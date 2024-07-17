package org.routineade.RoutineAdeServer.dto.routine;

import org.routineade.RoutineAdeServer.domain.Routine;

public record GroupRoutineDetail(
        Long routineId,
        String routineTitle,
        String routineCategory,
        Boolean isCompletion
) {
    public static GroupRoutineDetail of(Routine routine, Boolean isCompletion) {
        return new GroupRoutineDetail(routine.getRoutineId(), routine.getRoutineTitle(),
                routine.getRoutineCategory().getLabel(), isCompletion);
    }
}
