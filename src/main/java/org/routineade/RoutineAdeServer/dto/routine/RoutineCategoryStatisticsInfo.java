package org.routineade.RoutineAdeServer.dto.routine;

public record RoutineCategoryStatisticsInfo(
        String category,
        Integer completedCount
) {
    public static RoutineCategoryStatisticsInfo of(String category, Integer completedCount) {
        return new RoutineCategoryStatisticsInfo(category, completedCount);
    }
}
