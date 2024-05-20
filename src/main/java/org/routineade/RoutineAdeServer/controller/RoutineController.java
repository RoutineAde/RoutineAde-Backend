package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.routine.CheckRoutineRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineUpdateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesGetResponse;
import org.routineade.RoutineAdeServer.service.RoutineService;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "루틴 API", description = "루틴 관련 API")
@RestController
@RequestMapping("/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;
    private final UserService userService;

    @Operation(summary = "루틴 조회", description = "사용자의 루틴을 조회하는 API")
    @Parameters({
            @Parameter(name = "routineDate", description = "조회할 날짜", example = "2024.06.25")
    })
    @GetMapping
    public ResponseEntity<RoutinesGetResponse> getRoutines(Principal principal,
                                                           @RequestParam String routineDate) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        RoutinesGetResponse response = routineService.getRoutines(user, routineDate);

        return ResponseEntity
                .status(OK)
                .body(response);
    }

    @Operation(summary = "루틴 생성", description = "사용자의 루틴을 생성하는 API")
    @Parameters({
            @Parameter(name = "routineTitle", description = "루틴명", example = "하루 30분 운동하기"),
            @Parameter(name = "routineCategory", description = "루틴 카테고리 (DAILY, HEALTH, CARE, SELF_IMPROVEMENT, OTHER 중 하나)", example = "HEALTH"),
            @Parameter(name = "isAlarmEnabled", description = "루틴 알람 여부", example = "true"),
            @Parameter(name = "startDate", description = "루틴 시작 날짜", example = "2024.06.25"),
            @Parameter(name = "repeatDays", description = "루틴 반복 요일 (Mon, Tue, Wed, Thu, Fri, Sat, Sun 중 한 개 이상)", example = "[Mon, Wed, Sun]")
    })
    @PostMapping
    public ResponseEntity<Void> createRoutine(Principal principal,
                                              @Valid @RequestBody RoutineCreateRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        routineService.createRoutine(user, request);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @Operation(summary = "루틴 수정", description = "사용자의 루틴을 수정하는 API")
    @Parameters({
            @Parameter(name = "routineId", description = "수정할 루틴의 ID", example = "1"),
            @Parameter(name = "routineTitle", description = "루틴명", example = "하루 30분 운동하기"),
            @Parameter(name = "routineCategory", description = "루틴 카테고리 (DAILY, HEALTH, CARE, SELF_IMPROVEMENT, OTHER 중 하나)", example = "HEALTH"),
            @Parameter(name = "isAlarmEnabled", description = "루틴 알람 여부", example = "true"),
            @Parameter(name = "repeatDays", description = "루틴 반복 요일 (Mon, Tue, Wed, Thu, Fri, Sat, Sun 중 한 개 이상)", example = "[Mon, Wed, Sun]")
    })
    @PutMapping("/{routineId}")
    public ResponseEntity<Void> updateRoutine(Principal principal,
                                              @PathVariable Long routineId,
                                              @Valid @RequestBody RoutineUpdateRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        routineService.updateRoutine(user, routineId, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "루틴 삭제", description = "사용자의 루틴을 삭제하는 API")
    @Parameters({
            @Parameter(name = "routineId", description = "삭제할 루틴의 ID", example = "1")
    })
    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(Principal principal,
                                              @PathVariable Long routineId) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        routineService.deleteRoutine(user, routineId);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "루틴 수정", description = "사용자의 루틴을 수정하는 API")
    @Parameters({
            @Parameter(name = "routineId", description = "수정할 루틴의 ID", example = "1"),
            @Parameter(name = "checkRoutineDate", description = "루틴 체크할 날짜", example = "2024.06.25"),
    })
    @PutMapping("/{routineId}/check")
    public ResponseEntity<Void> checkRoutine(Principal principal,
                                             @PathVariable Long routineId,
                                             @Valid @RequestBody CheckRoutineRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        routineService.checkRoutine(user, routineId, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }
}
