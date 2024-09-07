package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;

public record GroupRoutinesCategoryInfo(
        String routineCategory,
        List<GroupRoutineInfo> routines
) {
    public static GroupRoutinesCategoryInfo of(String routineCategory, List<GroupRoutineInfo> routines) {
        return new GroupRoutinesCategoryInfo(routineCategory, routines);
    }
}
