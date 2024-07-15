package org.routineade.RoutineAdeServer.dto.groupChatting;

import java.util.List;

public record GroupChattingGetResponse(
        List<GroupChattingGetInfo> groupChatting
) {
    public static GroupChattingGetResponse of(List<GroupChattingGetInfo> groupChatting) {
        return new GroupChattingGetResponse(groupChatting);
    }
}
