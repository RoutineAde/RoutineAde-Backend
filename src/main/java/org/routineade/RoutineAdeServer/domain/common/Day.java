package org.routineade.RoutineAdeServer.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Day {
    Mon("월"),
    Tue("화"),
    Wed("수"),
    Thu("목"),
    Fri("금"),
    Sat("토"),
    Sun("일");

    private final String label;
}
