package org.routineade.RoutineAdeServer.dto.user;

import org.routineade.RoutineAdeServer.domain.User;

public record UserInfosGetResponse(
        String profileImage,
        String nickname,
        String intro,
        Long personalRoutineCount,
        Long groupRoutineCount,
        Long groupCount
) {
    public static UserInfosGetResponse of(User user, Long personalRoutineCount) {
        return new UserInfosGetResponse(user.getProfileImage(), user.getNickname(), user.getIntro(),
                personalRoutineCount,
                (long) user.getGroupMembers()
                        .stream()
                        .map(gm -> gm.getGroup().getGroupRoutines().size())
                        .mapToInt(i -> i)
                        .sum(),
                (long) user.getGroupMembers().size()
        );
    }
}
