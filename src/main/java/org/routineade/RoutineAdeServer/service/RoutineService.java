package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupMember;
import org.routineade.RoutineAdeServer.domain.GroupRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.routine.CompletionRoutineRequest;
import org.routineade.RoutineAdeServer.dto.routine.GroupRoutineInfo;
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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private final RoutineRepository routineRepository;
    private final RoutineRepeatDayService routineRepeatDayService;
    private final CompletionRoutineService completionRoutineService;
    private final UserEmotionService userEmotionService;
    private final GroupRoutineService groupRoutineService;

    @Transactional(readOnly = true)
    public RoutinesGetResponse getRoutines(User user, String routineDate) {
        LocalDate date = LocalDate.parse(routineDate, DATE_FORMATTER);
        List<Routine> personalRoutines = routineRepeatDayService.getPersonalRoutinesByDay(user,
                date.getDayOfWeek()); // 요일로 개인 루틴 구함

        /*
        개인 루틴
         */
        List<PersonalRoutineGetResponse> personalRoutineGetResponses = new ArrayList<>();
        for (Routine routine : personalRoutines) {
            if (routine.getStartDate().isBefore(date) || routine.getStartDate().isEqual(date)) {
                List<String> repeatDays = routine.getRoutineRepeatDays().stream()
                        .map(rd -> rd.getRepeatDay().getLabel()).toList();

                Boolean isCompletion = completionRoutineService.getIsCompletionRoutine(user, routine, date);

                personalRoutineGetResponses.add(PersonalRoutineGetResponse.of(
                        routine, repeatDays, routine.getStartDate().format(DATE_FORMATTER), isCompletion
                ));
            }
        }

        /*
        그룹 루틴
         */
        List<GroupRoutinesGetResponse> groupRoutinesGetResponses = new ArrayList<>();
        Set<Group> userGroups = user.getGroupMembers().stream().map(GroupMember::getGroup).collect(Collectors.toSet());
        for (Group userGroup : userGroups) {
            List<Routine> routines = userGroup.getGroupRoutines().stream().map(GroupRoutine::getRoutine).toList();
            List<Routine> filterRoutines = routineRepeatDayService.filterRoutinesByDay(routines, date.getDayOfWeek());

            List<GroupRoutineInfo> groupRoutineInfos = filterRoutines.stream()
                    .map(routine -> GroupRoutineInfo.of(routine,
                            completionRoutineService.getIsCompletionRoutine(user, routine, date)))
                    .toList();

            groupRoutinesGetResponses.add(GroupRoutinesGetResponse.of(userGroup, groupRoutineInfos));
        }

        return RoutinesGetResponse.of(personalRoutineGetResponses, groupRoutinesGetResponses,
                userEmotionService.getUserEmotionByDate(user, date));
    }

    public void createRoutine(User user, RoutineCreateRequest request) {
        LocalDate startDate = LocalDate.parse(request.startDate(), DATE_FORMATTER);
        if (startDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("루틴 시작일은 과거일 수 없습니다!");
        }

        Routine routine = Routine.builder()
                .createdUserId(user.getUserId())
                .routineTitle(request.routineTitle())
                .routineCategory(getCategoryByLabel(request.routineCategory()))
                .isAlarmEnabled(request.isAlarmEnabled())
                .startDate(startDate)
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

        routineRepeatDayService.updateRoutineRepeatDay(routine, request.repeatDays());

        routine.update(request.routineTitle(), getCategoryByLabel(request.routineCategory()), request.isAlarmEnabled(),
                LocalDate.parse(request.startDate(), DATE_FORMATTER));
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

        LocalDate routineDate = LocalDate.parse(request.date(), DATE_FORMATTER);

        if (routineDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("미래의 루틴을 완료할 수 없습니다!");
        }

        if (!routine.getIsPersonal()) { // 그룹 루틴인가?
            if (!groupRoutineService.userIsGroupIn(routine, user)) {
                throw new RuntimeException("유저가 해당 루틴이 있는 그룹의 멤버가 아닙니다!");
            }
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
