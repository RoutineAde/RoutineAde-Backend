package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Group;

public record GroupRoutinesGetResponse(
        Long groupId,
        String groupTitle,
        Boolean isAlarmEnabled,
        List<GroupRoutinesCategoryInfo> groupRoutines
) {
    public static GroupRoutinesGetResponse of(Group group, Boolean isAlarmEnabled,
                                              List<GroupRoutinesCategoryInfo> groupRoutines) {
        return new GroupRoutinesGetResponse(group.getGroupId(), group.getGroupTitle(), isAlarmEnabled, groupRoutines);
    }
}
