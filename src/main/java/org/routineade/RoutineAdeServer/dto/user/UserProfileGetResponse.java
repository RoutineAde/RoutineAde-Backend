package org.routineade.RoutineAdeServer.dto.user;

import org.routineade.RoutineAdeServer.domain.User;

public record UserProfileGetResponse(
        Long userId,
        String profileImage,
        String nickname,
        String intro
) {
    public static UserProfileGetResponse of(User user) {
        return new UserProfileGetResponse(user.getUserId(), user.getProfileImage(), user.getNickname(),
                user.getIntro());
    }
}
