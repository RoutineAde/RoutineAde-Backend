package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<Void> createDailyMood(@Valid @RequestBody CreateDailyMoodRequest request) {
        User user = userService.getUserOrException(2L);
        userHistoryService.createDailyMood(user, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }
}
