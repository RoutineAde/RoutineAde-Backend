package org.routineade.RoutineAdeServer.service;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.RoutineRepeatDay;
import org.routineade.RoutineAdeServer.domain.User;
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
        routineRepeatDayRepository.deleteAllByRoutine(routine);
        createRoutineRepeatDay(routine, repeatDays);
    }

    public List<Routine> getPersonalRoutinesByDay(User user, DayOfWeek dayOfWeek) {
        return routineRepeatDayRepository.findByUserAndDay(user.getUserId(), getDayOfDayOfWeek(dayOfWeek)).stream()
                .map(RoutineRepeatDay::getRoutine).toList();
    }

    public List<Routine> filterRoutinesByDay(List<Routine> routines, DayOfWeek dayOfWeek) {
        List<Routine> mutableRoutines = new ArrayList<>(routines);

        mutableRoutines.removeIf(
                routine -> !routine.getRoutineRepeatDays().stream().map(RoutineRepeatDay::getRepeatDay).toList()
                        .contains(getDayOfDayOfWeek(dayOfWeek)));

        return mutableRoutines;
    }

    private Day getDayOfDayOfWeek(DayOfWeek dayOfWeek) {
        return getDayOfLabel(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN));
    }

    private Day getDayOfLabel(String label) {
        return Arrays.stream(Day.values())
                .filter(d -> d.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("반복 요일 형식이 잘못되었습니다."));
    }

}
