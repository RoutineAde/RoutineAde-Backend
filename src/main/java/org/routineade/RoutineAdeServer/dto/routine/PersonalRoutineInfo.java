package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;

public record PersonalRoutineInfo(
        Long routineId,
        String routineTitle,
        List<String> repeatDays,
        Boolean isAlarmEnabled,
        String startDate,
        Boolean isCompletion
) {
    public static PersonalRoutineInfo of(Routine routine, List<String> repeatDays, String startDate,
                                         Boolean isCompletion) {
        return new PersonalRoutineInfo(routine.getRoutineId(), routine.getRoutineTitle(),
                repeatDays, routine.getIsAlarmEnabled(), startDate, isCompletion);
    }
}
