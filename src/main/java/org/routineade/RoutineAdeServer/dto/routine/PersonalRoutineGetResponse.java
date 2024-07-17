package org.routineade.RoutineAdeServer.dto.routine;

import org.routineade.RoutineAdeServer.domain.Routine;

public record PersonalRoutineGetResponse(
        Long routineId,
        String routineTitle,
        String routineCategory,
        Boolean isAlarmEnabled
) {
    public static PersonalRoutineGetResponse of(Routine routine) {
        return new PersonalRoutineGetResponse(routine.getRoutineId(), routine.getRoutineTitle(),
                routine.getRoutineCategory().getLabel(),
                routine.getIsAlarmEnabled());
    }
}
