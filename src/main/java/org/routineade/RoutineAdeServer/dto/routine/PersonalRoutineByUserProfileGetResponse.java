package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;

public record PersonalRoutineByUserProfileGetResponse(
        Long routineId,
        String routineTitle,
        List<String> repeatDays,
        String routineCategory,
        Boolean isCompletion
) {
    public static PersonalRoutineByUserProfileGetResponse of(Routine routine, List<String> repeatDays,
                                                             Boolean isCompletion) {
        return new PersonalRoutineByUserProfileGetResponse(routine.getRoutineId(), routine.getRoutineTitle(),
                repeatDays, routine.getRoutineCategory().getLabel(), isCompletion);
    }
}
