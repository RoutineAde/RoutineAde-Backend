package org.routineade.RoutineAdeServer.exception;

public record ErrorResult(
        String message
) {
    public static ErrorResult of(String message) {
        return new ErrorResult(message);
    }
}
