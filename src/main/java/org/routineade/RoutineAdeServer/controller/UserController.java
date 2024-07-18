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
import org.routineade.RoutineAdeServer.dto.userEmotion.UserEmotionCreateRequest;
import org.routineade.RoutineAdeServer.service.UserEmotionService;
import org.routineade.RoutineAdeServer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserEmotionService userEmotionService;

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

    @Operation(summary = "유저 감정 등록", description = "사용자가 특정 날짜에 감정을 등록하는 API")
    @Parameters({
            @Parameter(name = "date", description = "감정을 등록할 날짜", example = "2024.06.25"),
            @Parameter(name = "emotion", description = "등록할 감정 (GOOD, OK, SAD, ANGRY 중 하나)", example = "GOOD")
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

    @Operation(summary = "서버 상태 체크", description = "서버가 정상적으로 작동중인지 확인하는 API")
    @GetMapping("/health")
    public ResponseEntity<String> check() {
        return ResponseEntity
                .status(OK)
                .body("서버 호출 성공!");
    }

}
