package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;

public record PersonalRoutineGetResponse(
        Long routineId,
        String routineTitle,
        List<String> repeatDays,
        String routineCategory,
        Boolean isAlarmEnabled,
        String startDate,
        Boolean isCompletion
) {
    public static PersonalRoutineGetResponse of(Routine routine, List<String> repeatDays, String startDate,
                                                Boolean isCompletion) {
        return new PersonalRoutineGetResponse(routine.getRoutineId(), routine.getRoutineTitle(),
                repeatDays, routine.getRoutineCategory().getLabel(), routine.getIsAlarmEnabled(), startDate,
                isCompletion);
    }
}
