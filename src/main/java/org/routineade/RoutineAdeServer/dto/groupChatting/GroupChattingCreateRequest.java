package org.routineade.RoutineAdeServer.dto.groupChatting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record GroupChattingCreateRequest(
        @NotBlank(message = "채팅 내용은 비어 있거나, 공백일 수 없습니다.")
        @Size(max = 120, message = "채팅 내용은 10자를 초과할 수 없습니다.")
        String content,
        MultipartFile image
) {
}
