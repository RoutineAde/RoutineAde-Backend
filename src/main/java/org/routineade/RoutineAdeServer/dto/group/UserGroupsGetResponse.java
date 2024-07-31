package org.routineade.RoutineAdeServer.dto.group;

import java.util.List;

public record UserGroupsGetResponse(
        List<UserGroupInfo> groups
) {
    public static UserGroupsGetResponse of(List<UserGroupInfo> groups) {
        return new UserGroupsGetResponse(groups);
    }
}
