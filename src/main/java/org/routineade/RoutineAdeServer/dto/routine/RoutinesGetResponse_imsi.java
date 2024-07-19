package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.UserEmotion;
import org.routineade.RoutineAdeServer.domain.common.Emotion;

public record RoutinesGetResponse_imsi(
        List<PersonalRoutineGetResponse> personalRoutines,
        Emotion userEmotion
) {
    public static RoutinesGetResponse_imsi of(List<PersonalRoutineGetResponse> personalRoutines,
                                              UserEmotion userEmotion) {
        return new RoutinesGetResponse_imsi(personalRoutines, userEmotion == null ? null : userEmotion.getEmotion());
    }
}
