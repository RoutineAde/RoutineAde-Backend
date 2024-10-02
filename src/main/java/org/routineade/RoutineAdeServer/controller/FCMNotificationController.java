package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.dto.firebase.AlarmSendRequest;
import org.routineade.RoutineAdeServer.service.FCMNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "푸시 알림 테스트 API")
@RestController("/push")
@RequiredArgsConstructor
public class FCMNotificationController {

    private final FCMNotificationService fcmNotificationService;

    @Operation(summary = "푸시 알림 테스트용 API", description = "푸시 알림을 테스트해보는 API")
    @Parameters({
            @Parameter(name = "deviceToken", description = "푸시 알림 받을 기기 파이어베이스 token"),
            @Parameter(name = "content", description = "푸시 알림 내용")
    })
    @PostMapping()
    public ResponseEntity<Void> sendAlarm(@RequestBody AlarmSendRequest request) {
        fcmNotificationService.testSend(request);

        return ResponseEntity
                .status(OK)
                .build();
    }

}
