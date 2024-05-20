package org.routineade.RoutineAdeServer.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineUpdateRequest;
import org.routineade.RoutineAdeServer.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
//    private final AlarmService alarmService;

    @Transactional
    public void createRoutine(User user, RoutineCreateRequest request) {
        boolean[] isAlarmDays = setRepeatDays(request.repeatDays());

        Routine routine = Routine.builder()
                .user(user)
                .routineTitle(request.routineTitle())
                .routineCategory(request.routineCategory())
                .isAlarmEnabled(request.isAlarmEnabled())
                .startDate(request.startDate())
                .isAlarmDays(isAlarmDays)
                .build();

        routineRepository.save(routine);

//        LocalDate endOfWeekDate = LocalDate.now().with(DayOfWeek.SUNDAY);
//        LocalDate routineStartDate = LocalDate.parse(routine.getStartDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
//
//        if (routineStartDate.isBefore(endOfWeekDate) || routineStartDate.isEqual(endOfWeekDate)) {
//            alarmService.createAlarm(user, routine.getRoutineTitle(), routineStartDate, endOfWeekDate, request.repeatDays());
//        }
    }

    @Transactional
    public void updateRoutine(User user, Long routineId, RoutineUpdateRequest request) {
        Routine routine = getRoutineOrException(routineId);
        if (!routine.getUser().equals(user)) {
            throw new RuntimeException("자신의 루틴만 수정할 수 있습니다!");
        }

        boolean[] isAlarmDays = setRepeatDays(request.repeatDays());

        routine.updateValue(request.routineTitle(), request.routineCategory(), request.isAlarmEnabled(), isAlarmDays);
    }

    @Transactional
    public void deleteRoutine(User user, Long routineId) {
        Routine routine = getRoutineOrException(routineId);
        if (!routine.getUser().equals(user)) {
            throw new RuntimeException("자신의 루틴만 삭제할 수 있습니다!");
        }

        routineRepository.delete(routine);
    }

    public Routine getRoutineOrException(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(() ->
                new RuntimeException("해당 ID를 가진 루틴이 없습니다."));
    }


    private boolean[] setRepeatDays(List<String> repeatDays) {
        boolean[] isAlarm = new boolean[7];
        for (String repeatDay : repeatDays) {
            switch (repeatDay) {
                case "Mon" -> isAlarm[0] = true;
                case "Tue" -> isAlarm[1] = true;
                case "Wed" -> isAlarm[2] = true;
                case "Thu" -> isAlarm[3] = true;
                case "Fri" -> isAlarm[4] = true;
                case "Sat" -> isAlarm[5] = true;
                case "Sun" -> isAlarm[6] = true;
                default -> throw new RuntimeException("반복 요일은 1개 이상 선택해야 합니다.");
            }
        }
        return isAlarm;
    }

//    private static List<LocalDate> getWeekDates(LocalDate date) {
//        List<LocalDate> weekDates = new ArrayList<>();
//
//        // 주의 시작일 (월요일)
//        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
//
//        // 주의 종료일 (일요일)
//        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);
//
//        LocalDate current = startOfWeek;
//        while (!current.isAfter(endOfWeek)) {
//            weekDates.add(current);
//            current = current.plusDays(1);
//        }
//
//        return weekDates;
//    }


}
