package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.Routine.RoutineBuilder;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final AlarmService alarmService;

    @Transactional
    public void createRoutine(User user, RoutineCreateRequest request) {
        RoutineBuilder builder = Routine.builder()
                .user(user)
                .routineTitle(request.routineTitle())
                .routineCategory(request.routineCategory())
                .isAlarmEnabled(request.isAlarmEnabled())
                .startDate(request.startDate());

        setRepeatDays(builder, request.repeatDays());

        Routine routine = builder.build();

        routineRepository.save(routine);

        alarmService.createAlarm(user, routine.getRoutineTitle(), LocalDate.now());
    }

    private void setRepeatDays(RoutineBuilder builder, List<String> repeatDays) {
        for (String repeatDay : repeatDays) {
            switch (repeatDay) {
                case "Mon" -> builder.repeatMon(true);
                case "Tue" -> builder.repeatTue(true);
                case "Wed" -> builder.repeatWed(true);
                case "Thu" -> builder.repeatThu(true);
                case "Fri" -> builder.repeatFri(true);
                case "Sat" -> builder.repeatSat(true);
                case "Sun" -> builder.repeatSun(true);
                default -> throw new RuntimeException("반복 요일은 1개 이상 선택해야 합니다.");
            }
        }
    }
}
