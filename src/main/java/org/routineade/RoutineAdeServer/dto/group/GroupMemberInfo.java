package org.routineade.RoutineAdeServer.dto.group;

import org.routineade.RoutineAdeServer.domain.User;

public record GroupMemberInfo(
        Long userId,
        String nickname,
        String profileImage
) {

    public static GroupMemberInfo of(User user) {
        return new GroupMemberInfo(user.getUserId(), user.getNickname(), user.getProfileImage());
    }

}
