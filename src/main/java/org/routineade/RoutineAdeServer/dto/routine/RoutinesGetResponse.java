package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.UserEmotion;

public record RoutinesGetResponse(
        List<PersonalRoutineGetResponse> personalRoutines,
        List<GroupRoutinesGetResponse> groupRoutines,
        UserEmotion userEmotion
) {
    public static RoutinesGetResponse of(List<PersonalRoutineGetResponse> personalRoutines,
                                         List<GroupRoutinesGetResponse> groupRoutines,
                                         UserEmotion userEmotion) {
        return new RoutinesGetResponse(personalRoutines, groupRoutines, userEmotion);
    }
}
