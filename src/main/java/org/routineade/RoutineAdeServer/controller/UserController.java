package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.user.CreateDailyMoodRequest;
import org.routineade.RoutineAdeServer.service.UserHistoryService;
import org.routineade.RoutineAdeServer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserHistoryService userHistoryService;

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

    @Operation(summary = "감정 등록 API", description = "사용자가 특정 날짜에 감정을 등록하는 API")
    @Parameters({
            @Parameter(name = "date", description = "감정을 등록할 날짜", example = "2024.06.25"),
            @Parameter(name = "dailyMood", description = "등록할 감정 (GOOD, OK, SAD, ANGRY 중 하나)", example = "GOOD")
    })
    @PostMapping
    public ResponseEntity<Void> createDailyMood(Principal principal,
                                                @Valid @RequestBody CreateDailyMoodRequest request) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        userHistoryService.createDailyMood(user, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }
}
