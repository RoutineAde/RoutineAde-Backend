package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;

public record RoutinesGetResponse(
        List<PersonalRoutineGetResponse> personalRoutines,
        List<GroupRoutinesGetResponse> groupRoutines
) {
    public static RoutinesGetResponse of(List<PersonalRoutineGetResponse> personalRoutines,
                                         List<GroupRoutinesGetResponse> groupRoutines) {
        return new RoutinesGetResponse(personalRoutines, groupRoutines);
    }
}
