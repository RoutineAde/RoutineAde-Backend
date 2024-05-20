package org.routineade.RoutineAdeServer.dto.routine;

import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.common.Category;

public record RoutineDetail(
        Long routineId,
        String routineTitle,
        Category routineCategory,
        Boolean isAlarmEnabled
) {
    public static RoutineDetail of(Routine routine) {
        return new RoutineDetail(routine.getRoutineId(), routine.getRoutineTitle(), routine.getRoutineCategory(),
                routine.getIsAlarmEnabled());
    }
}
