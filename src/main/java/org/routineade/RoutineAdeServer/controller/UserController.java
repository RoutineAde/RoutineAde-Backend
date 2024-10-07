package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.firebase.UserFirebeseTokenSaveRequest;
import org.routineade.RoutineAdeServer.dto.user.UserEmotionCreateRequest;
import org.routineade.RoutineAdeServer.dto.user.UserInfosGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserIsFirstGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCalenderStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCategoryStatisticsGetResponse;
import org.routineade.RoutineAdeServer.service.KakaoService;
import org.routineade.RoutineAdeServer.service.RoutineService;
import org.routineade.RoutineAdeServer.service.UserEmotionService;
import org.routineade.RoutineAdeServer.service.UserService;
import org.springframework.http.HttpHeaders;
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

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoutineService routineService;
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

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API", hidden = true)
    @GetMapping("/login/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String code) throws URISyntaxException {
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = kakaoService.login(code);
        httpHeaders.setLocation(
                new URI("http://15.164.88.94/users/login/kakao?token=" + token));

        return ResponseEntity
                .status(FOUND)
                .headers(httpHeaders)
                .build();
    }

    @Operation(summary = "유저 기본 정보 등록", description = "사용자가 첫 가입 시 기본 정보를 등록하는 API")
    @Parameters({
            @Parameter(name = "profileImage", description = "프로필 이미지", example = "이미지 (바꾸지 않았다면 null)"),
            @Parameter(name = "nickname", description = "닉네임", example = "참치마요"),
            @Parameter(name = "intro", description = "소개글", example = "안녕하세요~! (null 가능)")
    })
    @PostMapping(value = "/infos", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUserInfos(Principal principal,
                                                @RequestPart String nickname,
                                                @RequestPart(required = false) String intro,
                                                @RequestPart(required = false) MultipartFile image) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        userService.createUserInfo(user, nickname, intro, image);

        return ResponseEntity
                .status(CREATED)
                .build();
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

    @Operation(summary = "유저 자신 프로필 정보 수정", description = "유저가 자신의 내정보를 수정하는 API")
    @Parameters({
            @Parameter(name = "profileImage", description = "프로필 이미지", example = "이미지 (바꾸지 않았다면 null)"),
            @Parameter(name = "nickname", description = "닉네임(중복x)", example = "행복하자"),
            @Parameter(name = "intro", description = "소개글(nullable)", example = "잘 부탁 드립니다 ^^")
    })
    @PutMapping(value = "/profile", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getUserProfile(Principal principal,
                                               @RequestPart String nickname,
                                               @RequestPart(required = false) String intro,
                                               @RequestPart(required = false) MultipartFile image) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        userService.updateUserProfile(user, nickname, intro, image);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "유저 Firebase Token 저장", description = "Push 알림용 유저의 Firebase Token을 DB에 저장하는 API")
    @Parameter(name = "token", description = "토큰")
    @PostMapping("/{userId}/token")
    public ResponseEntity<String> saveUserFirebaseToken(@PathVariable Long userId,
                                                        @Valid @RequestBody UserFirebeseTokenSaveRequest request) {
        User user = userService.getUserOrException(userId);
        userService.saveUserFirebaseToken(user, request);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @Operation(summary = "유저 내 정보 조회", description = "유저의 내 정보(프로필, 루틴&그룹 수)를 조회하는 API")
    @GetMapping("/infos")
    public ResponseEntity<UserInfosGetResponse> getUserInfos(Principal principal) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(userService.getUserInfos(user));
    }

    @Operation(summary = "유저 회원 탈퇴", description = "유저의 정보를 삭제하는 API")
    @DeleteMapping("/infos")
    public ResponseEntity<Void> deleteUserInfos(Principal principal) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));
        userService.deleteUserInfos(user);
        routineService.deleteRoutinesByUser(user);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @Operation(summary = "유저 최초로그인 확인", description = "유저가 최초로그인한 유저인지 확인하는 API \n\ntrue:최초, false:최초x")
    @GetMapping("/isFirst")
    public ResponseEntity<UserIsFirstGetResponse> getUserIsFirst(Principal principal) {
        User user = userService.getUserOrException(Long.valueOf(principal.getName()));

        return ResponseEntity
                .status(OK)
                .body(userService.getUserIsFirst(user));
    }

}
