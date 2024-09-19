package org.routineade.RoutineAdeServer.dto.groupChatting;

import java.util.List;

public record GroupChattingGetResponse(
        List<GroupChattingGetByDay> groupChatting
) {
    public static GroupChattingGetResponse of(List<GroupChattingGetByDay> groupChatting) {
        return new GroupChattingGetResponse(groupChatting);
    }
}
