package org.routineade.RoutineAdeServer.dto.group;

import java.util.List;

public record GroupsGetResponse(
        List<GroupBasicInfo> groups
) {
    public static GroupsGetResponse of(List<GroupBasicInfo> groups) {
        return new GroupsGetResponse(groups);
    }

}
