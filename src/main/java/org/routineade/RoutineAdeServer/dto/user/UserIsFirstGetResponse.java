package org.routineade.RoutineAdeServer.dto.user;

public record UserIsFirstGetResponse(
        boolean isFirst
) {
    public static UserIsFirstGetResponse of(boolean isFirst) {
        return new UserIsFirstGetResponse(isFirst);
    }
}
