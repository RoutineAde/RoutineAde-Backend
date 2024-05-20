package org.routineade.RoutineAdeServer.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.routine.CheckRoutineRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineUpdateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesGetRequest;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<RoutinesGetResponse> getRoutines(@Valid @RequestBody RoutinesGetRequest request) {
        User user = userService.getUserOrException(2L);
        RoutinesGetResponse response = routineService.getRoutines(user, request);

        return ResponseEntity
                .status(OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createRoutine(@Valid @RequestBody RoutineCreateRequest request) {
        User user = userService.getUserOrException(2L);
        routineService.createRoutine(user, request);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @PutMapping("/{routineId}")
    public ResponseEntity<Void> updateRoutine(@PathVariable Long routineId,
                                              @Valid @RequestBody RoutineUpdateRequest request) {
        User user = userService.getUserOrException(1L);
        routineService.updateRoutine(user, routineId, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long routineId) {
        User user = userService.getUserOrException(1L);
        routineService.deleteRoutine(user, routineId);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }

    @PutMapping("/{routineId}/check")
    public ResponseEntity<Void> checkRoutine(@PathVariable Long routineId,
                                             @Valid @RequestBody CheckRoutineRequest request) {
        User user = userService.getUserOrException(2L);
        routineService.checkRoutine(user, routineId, request);

        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }
}
