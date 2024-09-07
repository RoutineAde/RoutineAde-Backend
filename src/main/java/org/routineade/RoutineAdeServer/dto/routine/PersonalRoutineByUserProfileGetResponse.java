package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;

public record PersonalRoutineByUserProfileGetResponse(
        String routineCategory,
        List<PersonalRoutineByUserProfileInfo> routines
) {
    public static PersonalRoutineByUserProfileGetResponse of(String routineCategory,
                                                             List<PersonalRoutineByUserProfileInfo> routines) {
        return new PersonalRoutineByUserProfileGetResponse(routineCategory, routines);
    }
}
