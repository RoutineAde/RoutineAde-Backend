package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;

public record PersonalRoutineGetResponse(
        String routineCategory,
        List<PersonalRoutineInfo> routines
) {
    public static PersonalRoutineGetResponse of(String routineCategory, List<PersonalRoutineInfo> routines) {
        return new PersonalRoutineGetResponse(routineCategory, routines);
    }
}
