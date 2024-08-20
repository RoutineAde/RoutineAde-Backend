package org.routineade.RoutineAdeServer.dto.user;

import java.util.List;

public record UserRoutineCompletionStatistics(
        List<RoutineCompletionInfo> routineCompletionInfos
) {
    public static UserRoutineCompletionStatistics of(List<RoutineCompletionInfo> routineCompletionInfos) {
        return new UserRoutineCompletionStatistics(routineCompletionInfos);
    }
}
