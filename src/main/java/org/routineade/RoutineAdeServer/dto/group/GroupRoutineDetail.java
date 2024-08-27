package org.routineade.RoutineAdeServer.dto.group;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;

public record GroupRoutineDetail(
        Long routineId,
        String routineTitle,
        List<String> repeatDay
) {
    private static final List<String> DAY_ORDER = Arrays.asList("월", "화", "수", "목", "금", "토", "일");

    public static GroupRoutineDetail of(Routine routine, List<String> repeatDays) {
        // repeatDays 리스트를 요일 순서대로 정렬
        repeatDays.sort(Comparator.comparingInt(DAY_ORDER::indexOf));
        return new GroupRoutineDetail(routine.getRoutineId(), routine.getRoutineTitle(), repeatDays);
    }
}
