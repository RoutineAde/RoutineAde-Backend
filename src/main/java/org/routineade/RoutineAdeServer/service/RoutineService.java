package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupMember;
import org.routineade.RoutineAdeServer.domain.GroupRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.routine.CompletionRoutineRequest;
import org.routineade.RoutineAdeServer.dto.routine.GroupRoutineDetail;
import org.routineade.RoutineAdeServer.dto.routine.GroupRoutinesGetResponse;
import org.routineade.RoutineAdeServer.dto.routine.PersonalRoutineGetResponse;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineUpdateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesGetResponse;
import org.routineade.RoutineAdeServer.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineRepeatDayService routineRepeatDayService;
    private final CompletionRoutineService completionRoutineService;

    @Transactional(readOnly = true)
    public RoutinesGetResponse getRoutines(User user, String routineDate) {
        LocalDate date = LocalDate.parse(routineDate, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        List<Routine> personalRoutines = routineRepeatDayService.getPersonalRoutinesByDay(user, date.getDayOfWeek());

        List<PersonalRoutineGetResponse> personalRoutineGetResponses = new ArrayList<>();
        for (Routine routine : personalRoutines) {
            if (routine.getStartDate().isBefore(date) || routine.getStartDate().isEqual(date)) {
                personalRoutineGetResponses.add(PersonalRoutineGetResponse.of(routine));
            }
        }

        List<GroupRoutinesGetResponse> groupRoutinesGetResponses = new ArrayList<>();
        for (Group userGroup : user.getGroupMembers().stream().map(GroupMember::getGroup).collect(Collectors.toSet())) {
            List<Routine> routines = userGroup.getGroupRoutines().stream().map(GroupRoutine::getRoutine).toList();

            List<GroupRoutineDetail> groupRoutines = routineRepeatDayService.getRoutinesByDay(routines,
                            date.getDayOfWeek()).stream()
                    .map(r -> GroupRoutineDetail.of(r, completionRoutineService.getIsCompletionRoutine(user, r)))
                    .toList();

            groupRoutinesGetResponses.add(GroupRoutinesGetResponse.of(userGroup, groupRoutines));
        }

        return RoutinesGetResponse.of(personalRoutineGetResponses, groupRoutinesGetResponses);
    }

    public void createRoutine(User user, RoutineCreateRequest request) {
        Routine routine = Routine.builder()
                .createdUserId(user.getUserId())
                .routineTitle(request.routineTitle())
                .routineCategory(getCategoryByLabel(request.routineCategory()))
                .isAlarmEnabled(request.isAlarmEnabled())
                .startDate(LocalDate.parse(request.startDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();

        routineRepository.save(routine);

        routineRepeatDayService.createRoutineRepeatDay(routine, request.repeatDays());
    }

    public void updateRoutine(User user, Long routineId, RoutineUpdateRequest request) {
        Routine routine = getRoutineOrException(routineId);

        if (!routine.getCreatedUserId().equals(user.getUserId())) {
            throw new RuntimeException("자신의 루틴만 수정할 수 있습니다!");
        }

        if (!routine.getIsPersonal()) {
            throw new RuntimeException("개인 루틴만 수정할 수 있습니다!");
        }

        routine.update(request.routineTitle(), getCategoryByLabel(request.routineCategory()), request.isAlarmEnabled(),
                LocalDate.parse(request.startDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")));

        routineRepeatDayService.updateRoutineRepeatDay(routine, request.repeatDays());
    }

    public void deleteRoutine(User user, Long routineId) {
        Routine routine = getRoutineOrException(routineId);

        if (!routine.getCreatedUserId().equals(user.getUserId())) {
            throw new RuntimeException("자신의 루틴만 삭제할 수 있습니다!");
        }

        if (!routine.getIsPersonal()) {
            throw new RuntimeException("개인 루틴만 삭제할 수 있습니다!");
        }

        routineRepository.delete(routine);
    }

    public void setRoutineCompletionStatus(User user, Long routineId, CompletionRoutineRequest request) {
        Routine routine = getRoutineOrException(routineId);

        LocalDate routineDate = LocalDate.parse(request.date(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        if (routineDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("미래의 루틴을 완료할 수 없습니다!");
        }

        completionRoutineService.setCompletionRoutineStatus(user, routine, routineDate);
    }

    public Routine getRoutineOrException(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(() ->
                new RuntimeException("해당 ID를 가진 루틴이 없습니다."));
    }

    private Category getCategoryByLabel(String label) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("카테고리 형식이 잘못됐습니다!"));
    }

}
