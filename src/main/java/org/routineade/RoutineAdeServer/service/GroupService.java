package org.routineade.RoutineAdeServer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthenticationException;
import org.routineade.RoutineAdeServer.domain.CompletionRoutine;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupMember;
import org.routineade.RoutineAdeServer.domain.GroupRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.group.GroupBasicInfo;
import org.routineade.RoutineAdeServer.dto.group.GroupCreateRequest;
import org.routineade.RoutineAdeServer.dto.group.GroupGetResponse;
import org.routineade.RoutineAdeServer.dto.group.GroupInfo;
import org.routineade.RoutineAdeServer.dto.group.GroupMemberInfo;
import org.routineade.RoutineAdeServer.dto.group.GroupRoutineCategory;
import org.routineade.RoutineAdeServer.dto.group.GroupRoutineDetail;
import org.routineade.RoutineAdeServer.dto.group.GroupUpdateRequest;
import org.routineade.RoutineAdeServer.dto.group.GroupsGetResponse;
import org.routineade.RoutineAdeServer.dto.group.UserGroupInfo;
import org.routineade.RoutineAdeServer.dto.group.UserGroupsGetResponse;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingGetResponse;
import org.routineade.RoutineAdeServer.dto.groupRoutine.GroupRoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.groupRoutine.GroupRoutineUpdateRequest;
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
    private final UserService userService;
    private final CompletionRoutineService completionRoutineService;
    private final RoutineService routineService;

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
        Group group = getGroupOrThrowException(groupId);

        if (!Objects.equals(group.getCreatedUserId(), user.getUserId())) {
            throw new IllegalArgumentException("해당 그룹을 수정할 권한이 없습니다!");
        }

        if (group.getGroupMembers().size() > request.maxMember()) {
            throw new IllegalArgumentException("그룹 모집 인원은 현재 그룹원의 수보다 적을 수 없습니다!");
        }

        group.updateGroup(request.groupTitle(), request.groupPassword(), getCategoryByLabel(request.groupCategory()),
                request.maxMember(), request.description());
    }

    public void deleteGroup(User user, Long groupId) {
        Group group = getGroupOrThrowException(groupId);

        if (!Objects.equals(group.getCreatedUserId(), user.getUserId())) {
            throw new IllegalArgumentException("해당 그룹을 삭제할 권한이 없습니다!");
        }

        groupRepository.delete(group);
    }

    public void createGroupChatting(User user, Long groupId, String content, MultipartFile image) {
        Group group = getGroupOrThrowException(groupId);

        if (!groupMemberService.isMember(group, user)) {
            throw new IllegalArgumentException("해당 그룹에 채팅을 생성할 권한이 없습니다!");
        }

        groupChattingService.createGroupChatting(group, user, content, image);
    }

    @Transactional(readOnly = true)
    public GroupChattingGetResponse getGroupChatting(User user, Long groupId) {
        Group group = getGroupOrThrowException(groupId);

        if (!groupMemberService.isMember(group, user)) {
            throw new IllegalArgumentException("해당 그룹의 채팅을 조회할 권한이 없습니다!");
        }

        return groupChattingService.getGroupChatting(group, user);
    }

    @Transactional(readOnly = true)
    public UserGroupsGetResponse getUserGroups(User user) {
        List<UserGroupInfo> userGroupInfos = user.getGroupMembers()
                .stream()
                .sorted(Comparator.comparing(GroupMember::getGroupJoinDate).reversed())
                .map(GroupMember::getGroup)
                .map(g -> UserGroupInfo.of(g, userService.getUserOrException(g.getCreatedUserId()).getNickname(),
                        groupMemberService.getJoinDate(g, user)))
                .toList();

        return UserGroupsGetResponse.of(userGroupInfos);
    }

    @Transactional(readOnly = true)
    public GroupsGetResponse getGroups(User user, String groupCategory, Long groupCode, String keyword) {
        List<Group> groups = new ArrayList<>();

        if (groupCode != null) {
            if (groupCategory != null && !groupCategory.equals("전체")) {
                throw new IllegalArgumentException("그룹 코드로 검색 시 카테고리는 전체 또는 null 이어야 합니다!");
            }
            groupRepository.findById(groupCode).ifPresent(groups::add);
        } else if (keyword != null) {
            if (groupCategory != null && !groupCategory.equals("전체")) {
                throw new IllegalArgumentException("그룹 제목으로 검색 시 카테고리는 전체 또는 null 이어야 합니다!");
            }
            groups.addAll(groupRepository.findByKeyword(keyword));
        } else {
            groups.addAll(groupRepository.findByGroupCategory(getCategoryByLabel(groupCategory)));
        }

        return GroupsGetResponse.of(groups
                .stream()
                .sorted(Comparator.comparing(Group::getGroupId).reversed())
                .map(g -> GroupBasicInfo.of(g, userService.getUserOrException(g.getCreatedUserId()).getNickname(),
                        groupMemberService.isMember(g, user)))
                .toList());
    }

    public void joinGroup(User user, Long groupId, String password) throws AuthenticationException {
        Group group = getGroupOrThrowException(groupId);

        if (!group.getIsPublic()) {
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("비공개 그룹은 비밀번호를 입력해야 합니다!");
            }
            if (!group.getGroupPassword().equals(password)) {
                throw new AuthenticationException("그룹 비밀번호가 틀렸습니다.");
            }
        }

        groupMemberService.joinGroup(group, user);
    }

    public void unJoinGroup(User user, Long groupId) {
        Group group = getGroupOrThrowException(groupId);

        groupMemberService.unJoinGroup(group, user);

        List<Routine> groupRoutines = group.getGroupRoutines().stream().map(GroupRoutine::getRoutine).toList();
        List<Routine> completionGroupRoutines = user.getCompletionRoutines().stream().map(CompletionRoutine::getRoutine)
                .filter(groupRoutines::contains).toList();

        completionRoutineService.deleteCompletionRoutines(user, completionGroupRoutines);
    }

    @Transactional(readOnly = true)
    public GroupGetResponse getGroup(User user, Long groupId) {
        // 그룹을 찾거나 예외 발생
        Group group = getGroupOrThrowException(groupId);

        // 그룹 접근 권한 확인
        if (!groupMemberService.isMember(group, user)) {
            throw new IllegalArgumentException("해당 그룹에 접근할 권한이 없습니다!");
        }

        // 그룹장 여부 확인
        Boolean isGroupAdmin = Objects.equals(group.getCreatedUserId(), user.getUserId());

        // 그룹 정보 생성
        GroupInfo groupInfo = GroupInfo.of(group,
                userService.getUserOrException(group.getCreatedUserId()).getNickname());

        // 그룹 알림 설정 상태 확인
        Boolean isGroupAlarmEnabled = groupMemberService.isUserGroupAlarmEnabled(user, group);

        // 그룹원 정보 생성
        List<GroupMemberInfo> groupMemberInfo = group.getGroupMembers().stream()
                .map(GroupMember::getUser)
                .map(GroupMemberInfo::of)
                .collect(Collectors.toList());

        // 그룹 루틴 및 카테고리 정보 생성
        List<GroupRoutineCategory> groupRoutineCategories = createGroupRoutineCategories(
                group.getGroupRoutines().stream().map(GroupRoutine::getRoutine).collect(
                        Collectors.toSet())).stream().filter(grc -> !grc.routines().isEmpty()).toList();

        // GroupGetResponse 생성 및 반환
        return GroupGetResponse.of(isGroupAdmin, isGroupAlarmEnabled, groupInfo, groupMemberInfo,
                groupRoutineCategories);
    }

    public void createGroupRoutine(User user, Long groupId, GroupRoutineCreateRequest request) {
        Group group = getGroupOrThrowException(groupId);

        if (!Objects.equals(group.getCreatedUserId(), user.getUserId())) {
            throw new IllegalArgumentException("그룹 루틴은 루틴장만이 등록할 수 있습니다!");
        }

        routineService.createGroupRoutine(user, group, request);
    }

    public void updateGroupRoutine(User user, Long groupId, Long routineId, GroupRoutineUpdateRequest request) {
        Group group = getGroupOrThrowException(groupId);

        if (!Objects.equals(group.getCreatedUserId(), user.getUserId())) {
            throw new IllegalArgumentException("그룹 루틴은 루틴장만이 수정할 수 있습니다!");
        }

        routineService.updateGroupRoutine(group, routineId, request);
    }

    private Group getGroupOrThrowException(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 그룹이 존재하지 않습니다."));
    }

    private List<GroupRoutineCategory> createGroupRoutineCategories(Set<Routine> routines) {
        return Arrays.stream(Category.values())
                .map(category -> GroupRoutineCategory.of(
                        category.getLabel(),
                        routines.stream()
                                .filter(gr -> gr.getRoutineCategory().equals(category))
                                .map(r -> GroupRoutineDetail.of(
                                        r,
                                        r.getRoutineRepeatDays().stream()
                                                .map(rd -> rd.getRepeatDay().getLabel())
                                                .collect(Collectors.toList())))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    private Category getCategoryByLabel(String label) {
        if (label.equals("전체")) {
            return null;
        }
        return Arrays.stream(Category.values())
                .filter(category -> category.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("카테고리 형식이 잘못됐습니다!"));
    }

}
