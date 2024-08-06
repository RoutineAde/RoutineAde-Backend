package org.routineade.RoutineAdeServer.dto.group;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;

public record GroupRoutineDetail(
        Long routineId,
        String routineTitle,
        List<String> repeatDay
) {

    public static GroupRoutineDetail of(Routine routine, List<String> repeatDays) {
        return new GroupRoutineDetail(routine.getRoutineId(), routine.getRoutineTitle(), repeatDays);
    }

}
