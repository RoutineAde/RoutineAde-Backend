package org.routineade.RoutineAdeServer.service;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.RoutineRepeatDay;
import org.routineade.RoutineAdeServer.domain.common.Day;
import org.routineade.RoutineAdeServer.repository.RoutineRepeatDayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineRepeatDayService {

    private final RoutineRepeatDayRepository routineRepeatDayRepository;

    public void createRoutineRepeatDay(Routine routine, List<String> repeatDays) {
        repeatDays.stream()
                .map(this::getDayOfLabel)
                .forEach(day -> routineRepeatDayRepository.save(RoutineRepeatDay.of(day, routine)));
    }

    public void updateRoutineRepeatDay(Routine routine, List<String> repeatDays) {
        routineRepeatDayRepository.deleteAll(routine.getRoutineRepeatDays());
        
        createRoutineRepeatDay(routine, repeatDays);
    }

    private Day getDayOfLabel(String label) {
        return Arrays.stream(Day.values())
                .filter(d -> d.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("반복 요일 형식이 잘못되었습니다."));
    }

}
