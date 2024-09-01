package org.routineade.RoutineAdeServer.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserInfoCreateRequest(
        @NotBlank(message = "프로필 이미지는 비어 있거나, 공백일 수 없습니다.")
        @NotNull(message = "프로필 이미지는 null일 수 없습니다.")
        String profileImage,
        @NotBlank(message = "닉네임은 비어 있거나, 공백일 수 없습니다.")
        @NotNull(message = "닉네임은 null일 수 없습니다.")
        @Size(max = 10, message = "닉네임은 10글자 이하여야 합니다.")
        String nickname,
        @Size(max = 20, message = "소개글은 20글자 이하여야 합니다.")
        @Nullable
        String intro
) {
}
