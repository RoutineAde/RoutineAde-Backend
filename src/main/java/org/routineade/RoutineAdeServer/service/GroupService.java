package org.routineade.RoutineAdeServer.service;

import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.group.GroupCreateRequest;
import org.routineade.RoutineAdeServer.dto.group.GroupUpdateRequest;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingGetResponse;
import org.routineade.RoutineAdeServer.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;
    private final GroupChattingService groupChattingService;

    public void createGroup(User user, GroupCreateRequest request) {
        Group group = Group.builder()
                .groupTitle(request.groupTitle())
                .groupCategory(getCategoryByLabel(request.groupCategory()))
                .description(request.description())
                .maxMember(request.maxMember())
                .createdUserId(user.getUserId())
                .isPublic(request.groupPassword() == null)
                .groupPassword(request.groupPassword() == null ? null : request.groupPassword())
                .build();

        groupRepository.save(group);
        groupMemberService.createGroupMember(group, user);
    }

    public void updateGroup(User user, Long groupId, GroupUpdateRequest request) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new RuntimeException("해당 ID의 그룹이 존재하지 않습니다."));

        if (!Objects.equals(group.getCreatedUserId(), user.getUserId())) {
            throw new RuntimeException("해당 그룹을 수정할 권한이 없습니다!");
        }

        if (group.getGroupMembers().size() > request.maxMember()) {
            throw new RuntimeException("그룹 모집 인원은 현재 그룹원의 수보다 적을 수 없습니다!");
        }

        group.updateGroup(request.groupTitle(), request.groupPassword(), getCategoryByLabel(request.groupCategory()),
                request.maxMember(), request.description());
    }

    public void deleteGroup(User user, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new RuntimeException("해당 ID의 그룹이 존재하지 않습니다."));

        if (!Objects.equals(group.getCreatedUserId(), user.getUserId())) {
            throw new RuntimeException("해당 그룹을 삭제할 권한이 없습니다!");
        }

        groupRepository.delete(group);
    }

    public void createGroupChatting(User user, Long groupId, String content, MultipartFile image) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new RuntimeException("해당 ID의 그룹이 존재하지 않습니다."));

        if (groupMemberService.isNotMember(group, user)) {
            throw new RuntimeException("해당 그룹에 채팅을 생성할 권한이 없습니다!");
        }

        groupChattingService.createGroupChatting(group, user, content, image);
    }

    @Transactional(readOnly = true)
    public GroupChattingGetResponse getGroupChatting(User user, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new RuntimeException("해당 ID의 그룹이 존재하지 않습니다."));

        if (groupMemberService.isNotMember(group, user)) {
            throw new RuntimeException("해당 그룹의 채팅을 조회할 권한이 없습니다!");
        }

        return groupChattingService.getGroupChatting(group, user);
    }

    private Category getCategoryByLabel(String label) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("카테고리 형식이 잘못됐습니다!"));
    }

}
