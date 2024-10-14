package org.routineade.RoutineAdeServer.dto.user;

public record UserIsFirstGetResponse(
        boolean isFirst,
        String profileImage
) {
    public static UserIsFirstGetResponse of(boolean isFirst, String profileImage) {
        return new UserIsFirstGetResponse(isFirst, profileImage);
    }
}
