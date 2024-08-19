package org.routineade.RoutineAdeServer.dto.user;

import java.util.List;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCategoryStatisticsInfo;

public record UserRoutineCategoryStatisticsGetResponse(
        Integer completedRoutinesCount,
        List<RoutineCategoryStatisticsInfo> routineCategoryStatistics
) {
    public static UserRoutineCategoryStatisticsGetResponse of(Integer completedRoutinesCount,
                                                              List<RoutineCategoryStatisticsInfo> routineCategoryStatistics) {
        return new UserRoutineCategoryStatisticsGetResponse(completedRoutinesCount, routineCategoryStatistics);
    }
}
