package org.routineade.RoutineAdeServer.dto.routine;

import org.routineade.RoutineAdeServer.domain.Routine;

public record RoutineDetail(
        Long routineId,
        String routineTitle,
        String routineCategory,
        Boolean isAlarmEnabled
) {
    public static RoutineDetail of(Routine routine) {
        return new RoutineDetail(routine.getRoutineId(), routine.getRoutineTitle(),
                routine.getRoutineCategory().getLabel(),
                routine.getIsAlarmEnabled());
    }
}
