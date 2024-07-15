package org.routineade.RoutineAdeServer.dto.groupChatting;

import java.util.Objects;
import org.routineade.RoutineAdeServer.domain.GroupChatting;

public record GroupChattingGetInfo(
        String content,
        boolean isMine
) {
    public static GroupChattingGetInfo of(GroupChatting groupChatting, Long userId) {
        return new GroupChattingGetInfo(groupChatting.getContent(),
                Objects.equals(groupChatting.getWriterUserId(), userId));
    }
}
