package org.routineade.RoutineAdeServer.service;

import static org.routineade.RoutineAdeServer.domain.common.Category.CARE;
import static org.routineade.RoutineAdeServer.domain.common.Category.DAILY;
import static org.routineade.RoutineAdeServer.domain.common.Category.HEALTH;
import static org.routineade.RoutineAdeServer.domain.common.Category.OTHER;
import static org.routineade.RoutineAdeServer.domain.common.Category.SELF_IMPROVEMENT;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.group.GroupCreateRequest;
import org.routineade.RoutineAdeServer.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;

    public void createGroup(User user, GroupCreateRequest request) {
        Group group = Group.builder()
                .groupTitle(request.groupTitle())
                .groupCategory(extractCategory(request.groupCategory()))
                .description(request.description())
                .maxMember(request.maxMember())
                .createdUserId(user.getUserId())
                .isPublic(request.groupPassword() == null)
                .groupPassword(request.groupPassword() == null ? null : request.groupPassword())
                .build();

        groupRepository.save(group);
        groupMemberService.createGroupMember(group, user);
    }

    public void deleteGroup(User user, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new RuntimeException("해당 ID의 그룹이 존재하지 않습니다."));

        if (!Objects.equals(group.getCreatedUserId(), user.getUserId())) {
            throw new RuntimeException("해당 그룹을 삭제할 권한이 없습니다!");
        }

        groupRepository.delete(group);
    }

    private Category extractCategory(String groupCategory) {
        return switch (groupCategory) {
            case "daily" -> DAILY;
            case "health" -> HEALTH;
            case "care" -> CARE;
            case "self_improvement" -> SELF_IMPROVEMENT;
            case "other" -> OTHER;
            default -> throw new RuntimeException("카테고리가 잘못되었습니다!");
        };
    }

}
