package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.UserEmotion;
import org.routineade.RoutineAdeServer.domain.common.Emotion;

public record RoutinesByUserProfileGetResponse(
        List<PersonalRoutineByUserProfileGetResponse> personalRoutines,
        List<GroupRoutinesGetResponse> groupRoutines,
        Emotion userEmotion
) {
    public static RoutinesByUserProfileGetResponse of(List<PersonalRoutineByUserProfileGetResponse> personalRoutines,
                                                      List<GroupRoutinesGetResponse> groupRoutines,
                                                      UserEmotion userEmotion) {
        return new RoutinesByUserProfileGetResponse(personalRoutines, groupRoutines,
                userEmotion == null ? null : userEmotion.getEmotion());
    }
}
