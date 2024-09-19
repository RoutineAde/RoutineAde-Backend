package org.routineade.RoutineAdeServer.dto.groupChatting;

import static java.util.Locale.KOREAN;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record GroupChattingGetByDay(
        String createdDate,
        List<GroupChattingGetInfo> groupChatting
) {
    public static GroupChattingGetByDay of(LocalDateTime createdDate, List<GroupChattingGetInfo> groupChatting) {
        return new GroupChattingGetByDay(createdDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E)", KOREAN)),
                groupChatting);
    }
}
