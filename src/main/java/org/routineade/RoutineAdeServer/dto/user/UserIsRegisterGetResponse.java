package org.routineade.RoutineAdeServer.dto.user;

public record UserIsRegisterGetResponse(
        boolean isRegister
) {
    public static UserIsRegisterGetResponse of(boolean isRegister) {
        return new UserIsRegisterGetResponse(isRegister);
    }
}
