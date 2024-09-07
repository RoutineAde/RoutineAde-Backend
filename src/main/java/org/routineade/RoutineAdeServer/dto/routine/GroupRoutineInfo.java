package org.routineade.RoutineAdeServer.dto.routine;

import org.routineade.RoutineAdeServer.domain.Routine;

public record GroupRoutineInfo(
        Long routineId,
        String routineTitle,
        String routineCategory,
        Boolean isCompletion,
        Boolean isAlarmEnabled
) {
    public static GroupRoutineInfo of(Routine routine, Boolean isCompletion, Boolean isAlarmEnabled) {
        return new GroupRoutineInfo(routine.getRoutineId(), routine.getRoutineTitle(),
                routine.getRoutineCategory().getLabel(), isCompletion, isAlarmEnabled);
    }
}
