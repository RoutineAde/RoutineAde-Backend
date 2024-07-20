package org.routineade.RoutineAdeServer.dto.group;

import org.routineade.RoutineAdeServer.domain.Group;

public record GroupInfo(
        Long groupId,
        String groupTitle,
        String groupCategory,
        String createdUserNickname,
        Integer maxMemberCount,
        Integer joinMemberCount,
        Integer joinDate,
        Boolean isPublic
) {

    public static GroupInfo of(Group group, String createdUserNickname, Integer joinDate) {
        return new GroupInfo(group.getGroupId(), group.getGroupTitle(), group.getGroupCategory().getLabel(),
                createdUserNickname, group.getMaxMember(), group.getGroupMembers().size(), joinDate,
                group.getIsPublic());
    }
    
}
