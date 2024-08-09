package org.routineade.RoutineAdeServer.dto.group;

import org.routineade.RoutineAdeServer.domain.Group;

public record GroupBasicInfo(
        Long groupId,
        String groupTitle,
        String description,
        String groupCategory,
        String createdUserNickname,
        Integer maxMemberCount,
        Integer joinMemberCount,
        Boolean isPublic,
        Boolean isJoined
) {

    public static GroupBasicInfo of(Group group, String createdUserNickname, Boolean isJoined) {
        return new GroupBasicInfo(group.getGroupId(), group.getGroupTitle(), group.getDescription(),
                group.getGroupCategory().getLabel(), createdUserNickname, group.getMaxMember(),
                group.getGroupMembers().size(), group.getIsPublic(), isJoined);
    }

}
