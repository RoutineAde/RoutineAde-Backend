package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthenticationException;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.group.GroupCreateRequest;
import org.routineade.RoutineAdeServer.dto.group.GroupGetResponse;
import org.routineade.RoutineAdeServer.dto.group.GroupUpdateRequest;
import org.routineade.RoutineAdeServer.dto.group.GroupsGetResponse;
import org.routineade.RoutineAdeServer.dto.group.UserGroupsGetResponse;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingGetResponse;
import org.routineade.RoutineAdeServer.dto.groupRoutine.GroupRoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.groupRoutine.GroupRoutineUpdateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesByUserProfileGetResponse;
import org.routineade.RoutineAdeServer.service.GroupService;
import org.routineade.RoutineAdeServer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "그룹 API", description = "그룹 관련 API")
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    @Operation(summary = "그룹 생성", description = "그룹을 생성하는 API")
    @Parameters({
            @Parameter(name = "groupTitle", description = "그룹명", example = "꿈을 향해"),
            @Parameter(name = "groupPassword", description = "그룹 비밀번호 (없을 시 null)", example = "1234"),
            @Parameter(name = "groupCategory", description = "그룹 카테고리 (일상, 건강, 자기관리, 자기개발, 기타)", example = "건강"),
            @Parameter(name = "maxMember", description = "그룹 모집 인원수", example = "25"),
            @Parameter(name = "description", description = "그룹 소개", example = "그룹 소개입니당~")
    })
    @PostMapping
    public ResponseEntity<Void> createGroup(Principal principal,
                                            @RequestBody @Valid GroupCreateRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.createGroup(user, request);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @Operation(summary = "그룹 수정", description = "그룹을 수정하는 API")
    @Parameters({
            @Parameter(name = "groupTitle", description = "그룹명", example = "꿈을 향해"),
            @Parameter(name = "groupPassword", description = "그룹 비밀번호 (없을 시 null)", example = "null"),
            @Parameter(name = "groupCategory", description = "그룹 카테고리 (일상, 건강, 자기관리, 자기개발, 기타)", example = "건강"),
            @Parameter(name = "maxMember", description = "그룹 모집 인원수", example = "25"),
            @Parameter(name = "description", description = "그룹 소개", example = "그룹 소개입니당~")
    })
    @PutMapping("/{groupId}")
    public ResponseEntity<Void> updateGroup(Principal principal,
                                            @PathVariable("groupId") Long groupId,
                                            @Valid @RequestBody GroupUpdateRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.updateGroup(user, groupId, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "그룹 삭제", description = "그룹을 삭제하는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "삭제할 그룹 ID", example = "1")
    })
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(Principal principal,
                                            @PathVariable("groupId") Long groupId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.deleteGroup(user, groupId);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "그룹 채팅 생성", description = "그룹 채팅을 생성하는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "채팅을 생성할 그룹 ID", example = "1"),
            @Parameter(name = "content", description = "채팅 내용", example = "오늘 루틴 모두 완료했습니다^^"),
            @Parameter(name = "image", description = "채팅 첨부 이미지")
    })
    @PostMapping(value = "/{groupId}/chatting", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createGroupChatting(Principal principal,
                                                    @PathVariable("groupId") Long groupId,
                                                    @RequestPart(required = false) String content,
                                                    @RequestPart(required = false) MultipartFile image) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.createGroupChatting(user, groupId, content, image);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @Operation(summary = "그룹 채팅 조회", description = "그룹 채팅을 조회하는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "채팅을 조회할 그룹 ID", example = "1")
    })
    @GetMapping("/{groupId}/chatting")
    public ResponseEntity<GroupChattingGetResponse> getGroupChatting(Principal principal,
                                                                     @PathVariable("groupId") Long groupId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(groupService.getGroupChatting(user, groupId));
    }

    @Operation(summary = "내 그룹 조회", description = "로그인 한 유저의 그룹을 조회하는 API")
    @GetMapping("/my")
    public ResponseEntity<UserGroupsGetResponse> getUserGroups(Principal principal) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(groupService.getUserGroups(user));
    }

    @Operation(summary = "그룹 조회", description = "전체 그룹을 조회하는 API")
    @Parameters({
            @Parameter(name = "groupCategory", description = "조회할 그룹 카테고리 (전체, 일상, 건강, 자기관리, 자기개발, 기타, null)", example = "전체"),
            @Parameter(name = "groupCode", description = "조회할 그룹 코드 (없을 시 null)", example = "1"),
            @Parameter(name = "keyword", description = "그룹 제목 검색어 (없을 시 null)", example = "갓생러")
    })
    @GetMapping
    public ResponseEntity<GroupsGetResponse> getGroups(Principal principal,
                                                       @RequestParam(required = false) String groupCategory,
                                                       @RequestParam(required = false) Long groupCode,
                                                       @RequestParam(required = false) String keyword) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(groupService.getGroups(user, groupCategory, groupCode, keyword));
    }

    @Operation(summary = "그룹 가입", description = "그룹에 가입하는 API")
    @Parameter(name = "groupId", description = "가입할 그룹 ID", example = "1")
    @PostMapping("/{groupId}/join")
    public ResponseEntity<Void> joinGroup(Principal principal,
                                          @PathVariable Long groupId,
                                          @RequestParam(required = false) String password)
            throws AuthenticationException {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.joinGroup(user, groupId, password);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @Operation(summary = "그룹 기본 정보 조회", description = "그룹의 기본 정보를 조회하는 API \n\n"
            + "<조회되는 정보> \n\n"
            + "로그인 유저의 그룹장 여부(isGroupAdmin), \n\n"
            + "로그인 유저의 그룹 알림 여부(isGroupAlarmEnabled), \n\n"
            + "그룹 정보(groupInfo), \n\n"
            + "그룹원 정보(groupMembers), \n\n"
            + "그룹 루틴(groupRoutines)")
    @Parameter(name = "groupId", description = "조회할 그룹 ID", example = "1")
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupGetResponse> getGroup(Principal principal,
                                                     @PathVariable Long groupId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(groupService.getGroup(user, groupId));
    }

    @Operation(summary = "그룹 탈퇴", description = "그룹을 탈퇴하는 API")
    @Parameter(name = "groupId", description = "탈퇴할 그룹 ID", example = "1")
    @DeleteMapping("/{groupId}/un-join")
    public ResponseEntity<Void> unJoinGroup(Principal principal,
                                            @PathVariable Long groupId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.unJoinGroup(user, groupId);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "그룹 루틴 생성", description = "그룹에 루틴을 생성하는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "루틴 생성할 그룹 ID", example = "1"),
            @Parameter(name = "routineTitle", description = "그룹 루틴명", example = "하루 30분 운동하기"),
            @Parameter(name = "routineCategory", description = "그룹 루틴 카테고리 (일상, 건강, 자기관리, 자기개발, 기타 중 하나)", example = "건강"),
            @Parameter(name = "repeatDays", description = "그룹 루틴 반복 요일 (월, 화, 수, 목, 금, 토, 일 중 한 개 이상)", example = "[\"월\", \"수\", \"일\"]")
    })
    @PostMapping("/{groupId}/group-routines")
    public ResponseEntity<Void> createGroupRoutine(Principal principal,
                                                   @PathVariable Long groupId,
                                                   @RequestBody GroupRoutineCreateRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.createGroupRoutine(user, groupId, request);

        return ResponseEntity
                .status(OK)
                .build();
    }

    @Operation(summary = "그룹 루틴 수정", description = "그룹 루틴을 수정하는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "루틴 수정할 그룹 ID", example = "1"),
            @Parameter(name = "routineId", description = "수정할 루틴 ID", example = "35"),
            @Parameter(name = "routineTitle", description = "그룹 루틴명", example = "하루 30분 운동하기"),
            @Parameter(name = "routineCategory", description = "그룹 루틴 카테고리 (일상, 건강, 자기관리, 자기개발, 기타 중 하나)", example = "건강"),
            @Parameter(name = "repeatDays", description = "그룹 루틴 반복 요일 (월, 화, 수, 목, 금, 토, 일 중 한 개 이상)", example = "[\"월\", \"수\", \"일\"]")
    })
    @PutMapping("/{groupId}/group-routines/{routineId}")
    public ResponseEntity<Void> updateGroupRoutine(Principal principal,
                                                   @PathVariable Long groupId,
                                                   @PathVariable Long routineId,
                                                   @RequestBody GroupRoutineUpdateRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.updateGroupRoutine(user, groupId, routineId, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "그룹 루틴 삭제", description = "그룹 루틴을 삭제하는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "루틴 삭제할 그룹 ID", example = "1"),
            @Parameter(name = "routineId", description = "삭제할 루틴 ID", example = "35")
    })
    @DeleteMapping("/{groupId}/group-routines/{routineId}")
    public ResponseEntity<Void> deleteGroupRoutine(Principal principal,
                                                   @PathVariable Long groupId,
                                                   @PathVariable Long routineId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.deleteGroupRoutine(user, groupId, routineId);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "루틴원 내보내기", description = "루틴장이 루틴원을 강퇴시키는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "그룹 ID", example = "1"),
            @Parameter(name = "userId", description = "강퇴할 루틴원 ID", example = "2")
    })
    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeGroupMember(Principal principal,
                                                  @PathVariable Long groupId,
                                                  @PathVariable Long userId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.removeGroupMember(user, groupId, userId);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "그룹 알람 활성화 상태 변경", description = "사용자가 자신이 속한 그룹 알람의 활성화 상태를 변경하는 API\n\n"
            + "현재 true : false로 변경,\n\n현재 false : true로 변경.")
    @Parameter(name = "groupId", description = "그룹 ID", example = "1")
    @PutMapping("/{groupId}/alarm")
    public ResponseEntity<Void> updateGroupAlarm(Principal principal,
                                                 @PathVariable Long groupId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        groupService.updateGroupAlarm(user, groupId);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "그룹원 프로필 루틴 조회", description = "그룹원의 루틴을 조회하는 API")
    @Parameters({
            @Parameter(name = "groupId", description = "그룹 ID", example = "1"),
            @Parameter(name = "userId", description = "조회할 유저 ID", example = "1")
    })
    @GetMapping("{groupId}/users/{userId}/routines")
    public ResponseEntity<RoutinesByUserProfileGetResponse> getUserProfileRoutines(Principal principal,
                                                                                   @PathVariable Long groupId,
                                                                                   @PathVariable Long userId) {
        userService.getUserOrException(Long.valueOf(principal.getName()));
        User targetUser = userService.getUserOrException(userId);

        return ResponseEntity
                .status(OK)
                .body(groupService.getUserProfileRoutines(groupId, targetUser));
    }

}
