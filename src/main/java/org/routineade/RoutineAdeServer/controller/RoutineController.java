package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.OK;

import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.service.RoutineService;
import org.routineade.RoutineAdeServer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routine")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createRoutine(@RequestBody RoutineCreateRequest request) {
        User user = userService.getUserOrException(1L);
        routineService.createRoutine(user, request);

        return ResponseEntity
                .status(OK)
                .build();
    }
}
