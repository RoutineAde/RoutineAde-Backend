package org.routineade.RoutineAdeServer.dto.group;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GroupCreateRequest(
        @NotBlank(message = "그룹명은 비어 있거나, 공백일 수 없습니다.")
        @Size(max = 10, message = "그룹명은 10자를 초과할 수 없습니다.")
        String groupTitle,
        @Size(max = 4, message = "그룹 비밀번호는 4자를 초과할 수 없습니다.")
        String groupPassword,
        @NotNull(message = "그룹명은 필수값입니다.")
        String groupCategory,
        @Min(value = 2, message = "그룹 모집 인원은 최소 2명이어야 합니다.")
        @Max(value = 30, message = "그룹 모집 인원은 최대 30명이어야 합니다.")
        Integer maxMember,
        @NotBlank(message = "그룹 소개는 비어 있거나, 공백일 수 없습니다.")
        @Size(max = 100, message = "그룹소개는 100자를 초과할 수 없습니다.")
        String description
) {
}
