package org.routineade.RoutineAdeServer.dto.routine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.routineade.RoutineAdeServer.domain.common.Category;

public record RoutineUpdateRequest(
        @NotBlank(message = "루틴명은 비어 있거나, 공백일 수 없습니다.")
        @NotNull(message = "루틴명은 null일 수 없습니다.")
        @Size(min = 1, max = 15, message = "피드 내용은 15자를 초과할 수 없습니다.")
        String routineTitle,
        @NotNull(message = "카테고리는 1개 이상 선택해야 합니다.")
        Category routineCategory,
        @NotNull(message = "알람 여부는 null일 수 없습니다.")
        List<String> repeatDays,
        @NotNull(message = "반복 요일은 1개 이상 선택해야 합니다.")
        Boolean isAlarmEnabled
) {
}
