package org.routineade.RoutineAdeServer.dto.routine;

import org.routineade.RoutineAdeServer.domain.User;

public record RoutinesByUserProfileGetResponse(
        Long userId,
        String profileImage,
        String nickname,
        String intro,
        GroupRoutinesGetResponse groupRoutines
) {
    public static RoutinesByUserProfileGetResponse of(User user,
                                                      GroupRoutinesGetResponse groupRoutines) {
        return new RoutinesByUserProfileGetResponse(
                user.getUserId(),
                user.getProfileImage(),
                user.getNickname(),
                user.getIntro(),
                groupRoutines
        );
    }
}
