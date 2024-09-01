package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesByUserProfileGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserEmotionCreateRequest;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCalenderStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCategoryStatisticsGetResponse;
import org.routineade.RoutineAdeServer.service.KakaoService;
import org.routineade.RoutineAdeServer.service.UserEmotionService;
import org.routineade.RoutineAdeServer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserEmotionService userEmotionService;
    private final KakaoService kakaoService;

    @Operation(summary = "로그인", description = "로그인하여 인증용 토큰을 조회하는 API")
    @Parameters({
            @Parameter(name = "userId", description = "로그인할 유저 ID", example = "1")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody String userId) {
        String token = userService.login(Long.valueOf(userId));

        return ResponseEntity
                .status(OK)
                .body(Map.of("Authentication", token));
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) {
        return ResponseEntity
                .status(OK)
                .body(kakaoService.login(code));
    }

    @Operation(summary = "유저 감정 등록", description = "사용자가 특정 날짜에 감정을 등록하는 API")
    @Parameters({
            @Parameter(name = "date", description = "감정을 등록할 날짜", example = "2024.06.25"),
            @Parameter(name = "userEmotion", description = "등록할 감정 (GOOD, OK, SAD, ANGRY 중 하나)", example = "GOOD")
    })
    @PostMapping("/emotion")
    public ResponseEntity<Void> createUserEmotion(Principal principal,
                                                  @Valid @RequestBody UserEmotionCreateRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        userEmotionService.createUserEmotion(user, request);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @Operation(summary = "유저 월간 카테고리별 루틴 통계 조회", description = "사용자의 월간 카테고리별 루틴 통계를 조회하는 API")
    @Parameter(name = "date", description = "조회할 년월", example = "2024.09")
    @GetMapping("/statistics")
    public ResponseEntity<UserRoutineCategoryStatisticsGetResponse> getUserStatistics(Principal principal,
                                                                                      @RequestParam String date) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(userService.getUserStatistics(user, date));
    }

    @Operation(summary = "유저 월간 루틴 달성 캘린더 통계 조회", description = "사용자의 월간 루틴 달성 캘린더 통계를 조회하는 API")
    @Parameter(name = "date", description = "조회할 년월", example = "2024.09")
    @GetMapping("/statistics/calender")
    public ResponseEntity<UserRoutineCalenderStatisticsGetResponse> getUserCalenderStatistics(Principal principal,
                                                                                              @RequestParam String date) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(userService.getUserCalenderStatistics(user, date));
    }

    @Operation(summary = "타유저 프로필 루틴 조회", description = "타유저의 프로필의 루틴을 조회하는 API")
    @Parameter(name = "userId", description = "조회할 유저 ID", example = "1")
    @GetMapping("/{userId}/routines")
    public ResponseEntity<RoutinesByUserProfileGetResponse> getUserProfileRoutines(Principal principal,
                                                                                   @PathVariable Long userId) {
        userService.getUserOrException(Long.valueOf(principal.getName()));
        User targetUser = userService.getUserOrException(userId);

        return ResponseEntity
                .status(OK)
                .body(userService.getUserProfileRoutines(targetUser));
    }

    @Operation(summary = "타유저 프로필 캘린더 통계 조회", description = "타유저 프로필의 캘린더 통계를 조회하는 API")
    @Parameters({
            @Parameter(name = "userId", description = "조회할 유저 ID", example = "1"),
            @Parameter(name = "date", description = "조회할 년월", example = "2024.08")
    })
    @GetMapping("/{userId}/statistics/calender")
    public ResponseEntity<UserRoutineCalenderStatisticsGetResponse> getUserProfileCalender(Principal principal,
                                                                                           @PathVariable Long userId,
                                                                                           @RequestParam String date) {
        userService.getUserOrException(Long.valueOf(principal.getName()));
        User targetUser = userService.getUserOrException(userId);

        return ResponseEntity
                .status(OK)
                .body(userService.getUserCalenderStatistics(targetUser, date));
    }

    @Operation(summary = "타유저 프로필 카테고리별 통계 조회", description = "타유저 프로필의 카테고리별 통계를 조회하는 API")
    @Parameters({
            @Parameter(name = "userId", description = "조회할 유저 ID", example = "1"),
            @Parameter(name = "date", description = "조회할 년월", example = "2024.08")
    })
    @GetMapping("/{userId}/statistics")
    public ResponseEntity<UserRoutineCategoryStatisticsGetResponse> getUserProfileStatistics(Principal principal,
                                                                                             @PathVariable Long userId,
                                                                                             @RequestParam String date) {
        userService.getUserOrException(Long.valueOf(principal.getName()));
        User targetUser = userService.getUserOrException(userId);

        return ResponseEntity
                .status(OK)
                .body(userService.getUserStatistics(targetUser, date));
    }

}
