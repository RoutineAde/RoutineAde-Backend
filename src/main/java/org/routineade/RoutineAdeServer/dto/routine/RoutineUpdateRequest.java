package org.routineade.RoutineAdeServer.dto.routine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RoutineUpdateRequest(
        @NotBlank(message = "루틴명은 비어 있거나, 공백일 수 없습니다.")
        @NotNull(message = "루틴명은 null일 수 없습니다.")
        @Size(min = 1, max = 15, message = "루틴명은 15자를 초과할 수 없습니다.")
        String routineTitle,
        @NotBlank(message = "카테고리는 비어 있거나, 공백일 수 없습니다.")
        @NotNull(message = "카테고리는 1개 이상 선택해야 합니다.")
        String routineCategory,
        @NotNull(message = "반복 요일은 1개 이상 선택해야 합니다.")
        List<String> repeatDays,
        @NotNull(message = "알람 여부는 null일 수 없습니다.")
        Boolean isAlarmEnabled
) {
}
