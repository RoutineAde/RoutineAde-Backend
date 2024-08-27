package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.UserEmotion;
import org.routineade.RoutineAdeServer.domain.common.Emotion;

public record RoutinesByUserProfileGetResponse(
        Long userId,
        String profileImage,
        String nickname,
        String intro,
        List<PersonalRoutineByUserProfileGetResponse> personalRoutines,
        List<GroupRoutinesGetResponse> groupRoutines,
        Emotion userEmotion
) {
    public static RoutinesByUserProfileGetResponse of(User user,
                                                      List<PersonalRoutineByUserProfileGetResponse> personalRoutines,
                                                      List<GroupRoutinesGetResponse> groupRoutines,
                                                      UserEmotion userEmotion) {
        return new RoutinesByUserProfileGetResponse(
                user.getUserId(),
                user.getProfileImage(),
                user.getNickname(),
                user.getIntro(),
                personalRoutines, groupRoutines,
                userEmotion == null ? null : userEmotion.getEmotion()
        );
    }
}
