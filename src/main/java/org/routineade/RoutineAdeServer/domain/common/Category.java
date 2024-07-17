package org.routineade.RoutineAdeServer.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    DAILY("일상"),
    HEALTH("건강"),
    CARE("자기관리"),
    SELF_IMPROVEMENT("자기개발"),
    OTHER("기타");

    private final String label;
}
