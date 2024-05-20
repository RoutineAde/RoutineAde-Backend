package org.routineade.RoutineAdeServer.dto.routine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckRoutineRequest(
        @NotNull(message = "날짜는 null일 수 없습니다.")
        @NotBlank(message = "날짜는 비어있거나 공백일 수 없습니다.")
        String checkRoutineDate
) {
}
