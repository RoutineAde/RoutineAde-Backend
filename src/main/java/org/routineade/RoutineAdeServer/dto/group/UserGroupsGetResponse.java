package org.routineade.RoutineAdeServer.dto.group;

import java.util.List;

public record UserGroupsGetResponse(
        List<GroupInfo> groups
) {
    public static UserGroupsGetResponse of(List<GroupInfo> groups) {
        return new UserGroupsGetResponse(groups);
    }
}
