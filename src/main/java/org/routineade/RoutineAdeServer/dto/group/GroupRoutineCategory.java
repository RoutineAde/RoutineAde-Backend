package org.routineade.RoutineAdeServer.dto.group;

import java.util.List;

public record GroupRoutineCategory(
        String routineCategory,
        List<GroupRoutineDetail> routines
) {

    public static GroupRoutineCategory of(String routineCategory, List<GroupRoutineDetail> routines) {
        return new GroupRoutineCategory(routineCategory, routines);
    }

}
