package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Group;

public record GroupRoutinesGetResponse(
        Long groupId,
        String groupTitle,
        List<GroupRoutinesCategoryInfo> groupRoutines
) {
    public static GroupRoutinesGetResponse of(Group group, List<GroupRoutinesCategoryInfo> groupRoutines) {
        return new GroupRoutinesGetResponse(group.getGroupId(), group.getGroupTitle(), groupRoutines);
    }
}
