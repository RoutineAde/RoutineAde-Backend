package org.routineade.RoutineAdeServer.dto.group;

import java.util.List;

public record GroupGetResponse(
        Boolean isGroupAdmin,
        Boolean isGroupAlarmEnabled,
        GroupInfo groupInfo,
        List<GroupMemberInfo> groupMembers,
        List<GroupRoutineCategory> groupRoutines
) {

    public static GroupGetResponse of(Boolean isGroupAdmin, Boolean isGroupAlarmEnabled, GroupInfo groupInfo,
                                      List<GroupMemberInfo> groupMemberInfos,
                                      List<GroupRoutineCategory> groupRoutines) {
        return new GroupGetResponse(isGroupAdmin, isGroupAlarmEnabled, groupInfo, groupMemberInfos, groupRoutines);
    }

}
