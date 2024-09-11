package org.routineade.RoutineAdeServer.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.dto.firebase.FCMNotificationRequest;
import org.routineade.RoutineAdeServer.service.FCMNotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class FCMNotificationController {

    private final FCMNotificationService fcmNotificationService;

    @Operation(hidden = true)
    @PostMapping
    public void sendNotificationByToken(@RequestBody FCMNotificationRequest request) {
        fcmNotificationService.sendNotificationByToken(request);
    }

}
