package org.routineade.RoutineAdeServer.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.routine.CheckRoutineRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineDetail;
import org.routineade.RoutineAdeServer.dto.routine.RoutineUpdateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesGetRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesGetResponse;
import org.routineade.RoutineAdeServer.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final UserHistoryService userHistoryService;
//    private final AlarmService alarmService;

    @Transactional
    public void createRoutine(User user, RoutineCreateRequest request) {
        boolean[] isRepeatDays = setRepeatDays(request.repeatDays());

        Routine routine = Routine.builder()
                .user(user)
                .routineTitle(request.routineTitle())
                .routineCategory(request.routineCategory())
                .isAlarmEnabled(request.isAlarmEnabled())
                .startDate(request.startDate().replaceAll("\\.", "-"))
                .isRepeatDays(isRepeatDays)
                .build();

        routineRepository.save(routine);
    }

    @Transactional
    public void updateRoutine(User user, Long routineId, RoutineUpdateRequest request) {
        Routine routine = getRoutineOrException(routineId);
        if (!routine.getUser().equals(user)) {
            throw new RuntimeException("자신의 루틴만 수정할 수 있습니다!");
        }

        boolean[] isRepeatDays = setRepeatDays(request.repeatDays());

        routine.updateValue(request.routineTitle(), request.routineCategory(), request.isAlarmEnabled(), isRepeatDays);
    }

    @Transactional
    public void deleteRoutine(User user, Long routineId) {
        Routine routine = getRoutineOrException(routineId);
        if (!routine.getUser().equals(user)) {
            throw new RuntimeException("자신의 루틴만 삭제할 수 있습니다!");
        }

        routineRepository.delete(routine);
    }

    @Transactional
    public void checkRoutine(User user, Long routineId, CheckRoutineRequest request) {
        Routine routine = getRoutineOrException(routineId);
        if (!routine.getUser().equals(user)) {
            throw new RuntimeException("자신의 루틴만 체크할 수 있습니다!");
        }

        LocalDate routineDate = LocalDate.parse(request.checkRoutineDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        if (routineDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("미래의 루틴을 완료할 수 없습니다!");
        }

        userHistoryService.checkRoutine(user, routineDate, routineId);
    }

    @Transactional(readOnly = true)
    public RoutinesGetResponse getRoutines(User user, RoutinesGetRequest request) {
        LocalDate routineDate = LocalDate.parse(request.routineDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        List<Routine> dayRoutines = choiceRoutineSearchMethod(user, routineDate.getDayOfWeek());

        List<RoutineDetail> routines = new ArrayList<>();
        for (Routine routine : dayRoutines) {
            LocalDate routineStartDate = LocalDate.parse(routine.getStartDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (routineStartDate.isBefore(routineDate) || routineStartDate.isEqual(routineDate)) {
                routines.add(RoutineDetail.of(routine));
            }
        }

        return RoutinesGetResponse.of(routines);
    }

    public Routine getRoutineOrException(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(() ->
                new RuntimeException("해당 ID를 가진 루틴이 없습니다."));
    }

    private boolean[] setRepeatDays(List<String> repeatDays) {
        boolean[] isRepeat = new boolean[7];
        for (String repeatDay : repeatDays) {
            switch (repeatDay) {
                case "Mon" -> isRepeat[0] = true;
                case "Tue" -> isRepeat[1] = true;
                case "Wed" -> isRepeat[2] = true;
                case "Thu" -> isRepeat[3] = true;
                case "Fri" -> isRepeat[4] = true;
                case "Sat" -> isRepeat[5] = true;
                case "Sun" -> isRepeat[6] = true;
                default -> throw new RuntimeException("반복 요일은 1개 이상 선택해야 합니다.");
            }
        }
        return isRepeat;
    }

    private List<Routine> choiceRoutineSearchMethod(User user, DayOfWeek day) {
        switch (day) {
            case MONDAY -> {
                return routineRepository.findByUserAndMonDay(user);
            }
            case TUESDAY -> {
                return routineRepository.findByUserAndTueDay(user);
            }
            case WEDNESDAY -> {
                return routineRepository.findByUserAndWedDay(user);
            }
            case THURSDAY -> {
                return routineRepository.findByUserAndThuDay(user);
            }
            case FRIDAY -> {
                return routineRepository.findByUserAndFriDay(user);
            }
            case SATURDAY -> {
                return routineRepository.findByUserAndSatDay(user);
            }
            case SUNDAY -> {
                return routineRepository.findByUserAndSunDay(user);
            }
            default -> throw new RuntimeException("요일이 이상합니다!");
        }
    }

}
