package org.routineade.RoutineAdeServer.dto.group;

import java.util.List;

public record GroupsGetResponse(
        List<GroupInfo> groups
) {
    public static GroupsGetResponse of(List<GroupInfo> groups) {
        return new GroupsGetResponse(groups);
    }
}
