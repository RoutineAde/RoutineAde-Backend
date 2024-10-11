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
        Emotion userEmotion,
        List<GroupRoutinesGetResponse> userGroupInfo
) {
    public static RoutinesByUserProfileGetResponse of(User user,
                                                      UserEmotion userEmotion,
                                                      List<GroupRoutinesGetResponse> userGroupInfo) {
        return new RoutinesByUserProfileGetResponse(
                user.getUserId(),
                user.getProfileImage(),
                user.getNickname(),
                user.getIntro(),
                userEmotion == null ? null : userEmotion.getEmotion(),
                userGroupInfo
        );
    }
}
