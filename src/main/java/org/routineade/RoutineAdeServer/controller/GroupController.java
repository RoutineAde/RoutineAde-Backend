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
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.group.GroupCreateRequest;
import org.routineade.RoutineAdeServer.dto.group.GroupUpdateRequest;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingGetResponse;
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

}
