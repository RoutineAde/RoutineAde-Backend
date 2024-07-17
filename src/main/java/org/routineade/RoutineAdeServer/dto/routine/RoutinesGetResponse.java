package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;

public record RoutinesGetResponse(
        List<RoutineDetail> routines
) {
    public static RoutinesGetResponse of(List<RoutineDetail> routines) {
        return new RoutinesGetResponse(routines);
    }
}
