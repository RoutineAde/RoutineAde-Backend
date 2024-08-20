package org.routineade.RoutineAdeServer.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoutineCompletionLevel {

    NONE(0, 0.0, 0.0),
    LOW(1, 0.0, 1.0 / 3.0),
    MEDIUM(2, 1.0 / 3.0, 2.0 / 3.0),
    HIGH(3, 2.0 / 3.0, 1.0);

    private final int level;
    private final double minRate;
    private final double maxRate;

    public static RoutineCompletionLevel fromRate(double rate) {
        for (RoutineCompletionLevel level : values()) {
            if (rate >= level.minRate && rate <= level.maxRate) {
                return level;
            }
        }
        return NONE;
    }

}
