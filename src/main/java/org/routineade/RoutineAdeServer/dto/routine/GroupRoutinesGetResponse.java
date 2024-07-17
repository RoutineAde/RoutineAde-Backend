package org.routineade.RoutineAdeServer.dto.routine;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Group;

public record GroupRoutinesGetResponse(
        Long groupId,
        String groupTitle,
        List<GroupRoutineDetail> groupRoutines
) {
    public static GroupRoutinesGetResponse of(Group group, List<GroupRoutineDetail> groupRoutines) {
        return new GroupRoutinesGetResponse(group.getGroupId(), group.getGroupTitle(), groupRoutines);
    }
}
