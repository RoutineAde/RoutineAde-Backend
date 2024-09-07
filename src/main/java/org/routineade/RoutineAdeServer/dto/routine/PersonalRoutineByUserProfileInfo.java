package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;

public record PersonalRoutineByUserProfileInfo(
        Long routineId,
        String routineTitle,
        List<String> repeatDays,
        Boolean isCompletion
) {
    public static PersonalRoutineByUserProfileInfo of(Routine routine, List<String> repeatDays,
                                                      Boolean isCompletion) {
        return new PersonalRoutineByUserProfileInfo(routine.getRoutineId(), routine.getRoutineTitle(),
                repeatDays, isCompletion);
    }
}
