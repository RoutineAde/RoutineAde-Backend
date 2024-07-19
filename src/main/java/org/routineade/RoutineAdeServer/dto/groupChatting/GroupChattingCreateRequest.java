package org.routineade.RoutineAdeServer.dto.groupChatting;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record GroupChattingCreateRequest(
        @Size(max = 120, message = "채팅 내용은 10자를 초과할 수 없습니다.")
        String content,
        MultipartFile image
) {
}
