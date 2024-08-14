package org.routineade.RoutineAdeServer.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> InvalidRequestBodyExceptionHandler(IllegalArgumentException e) {
        log.error("[MethodArgumentNotValidException] exception ", e);
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResult.of("에러 : " + e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResult> AuthenticationExceptionHandler(AuthenticationException e) {
        log.error("[AuthenticationException] exception ", e);
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ErrorResult.of("에러 : " + e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResult> AuthenticationExceptionHandler(IllegalStateException e) {
        log.error("[IllegalStateException] exception ", e);
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ErrorResult.of("에러 : " + e.getMessage()));
    }

}
