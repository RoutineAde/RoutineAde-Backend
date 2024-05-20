package org.routineade.RoutineAdeServer.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.routineade.RoutineAdeServer.domain.Mood;

public record CreateDailyMoodRequest(
        @NotNull(message = "날짜는 null일 수 없습니다.")
        @NotBlank(message = "날짜는 비어있거나 공백일 수 없습니다.")
        String date,
        @NotNull(message = "감정은 null일 수 없습니다.")
        Mood dailyMood
) {
}
