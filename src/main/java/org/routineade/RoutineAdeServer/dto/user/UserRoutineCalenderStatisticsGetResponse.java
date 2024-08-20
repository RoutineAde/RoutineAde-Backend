package org.routineade.RoutineAdeServer.dto.user;

public record UserRoutineCalenderStatisticsGetResponse(
        Integer completedRoutinesCount,
        UserRoutineCompletionStatistics userRoutineCompletionStatistics
) {
    public static UserRoutineCalenderStatisticsGetResponse of(Integer completedRoutinesCount,
                                                              UserRoutineCompletionStatistics userRoutineCompletionStatistics) {
        return new UserRoutineCalenderStatisticsGetResponse(completedRoutinesCount, userRoutineCompletionStatistics);
    }
}
