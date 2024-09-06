package org.routineade.RoutineAdeServer.service;

import static java.util.Locale.KOREAN;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.CompletionRoutine;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupMember;
import org.routineade.RoutineAdeServer.domain.GroupRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.domain.common.RoutineCompletionLevel;
import org.routineade.RoutineAdeServer.dto.groupRoutine.GroupRoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.groupRoutine.GroupRoutineUpdateRequest;
import org.routineade.RoutineAdeServer.dto.routine.CompletionRoutineRequest;
import org.routineade.RoutineAdeServer.dto.routine.GroupRoutineInfo;
import org.routineade.RoutineAdeServer.dto.routine.GroupRoutinesGetResponse;
import org.routineade.RoutineAdeServer.dto.routine.PersonalRoutineByUserProfileGetResponse;
import org.routineade.RoutineAdeServer.dto.routine.PersonalRoutineGetResponse;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCreateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineUpdateRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesByUserProfileGetResponse;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesGetResponse;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesGetResponse_v2;
import org.routineade.RoutineAdeServer.dto.user.RoutineCompletionInfo;
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

    public List<PersonalRoutineGetResponse> getRoutines_v1(User user, String routineDate) {
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

        return personalRoutineGetResponses;
    }

    @Transactional(readOnly = true)
    public RoutinesGetResponse_v2 getRoutines_v2(User user, String routineDate) {
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

        return RoutinesGetResponse_v2.of(personalRoutineGetResponses,
                userEmotionService.getUserEmotionByDate(user, date));
    }

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

            if (filterRoutines.isEmpty()) {
                continue;
            }

            List<GroupRoutineInfo> groupRoutineInfos = filterRoutines.stream()
                    .map(routine -> GroupRoutineInfo.of(routine,
                            completionRoutineService.getIsCompletionRoutine(user, routine, date)))
                    .toList();

            groupRoutinesGetResponses.add(GroupRoutinesGetResponse.of(userGroup, groupRoutineInfos));
        }

        return RoutinesGetResponse.of(personalRoutineGetResponses, groupRoutinesGetResponses,
                userEmotionService.getUserEmotionByDate(user, date));
    }

    @Transactional(readOnly = true)
    public RoutinesByUserProfileGetResponse getRoutinesByUserProfile(User user) {
        LocalDate date = LocalDate.now();
        List<Routine> personalRoutines = routineRepeatDayService.getPersonalRoutinesByDay(user,
                date.getDayOfWeek()); // 요일로 개인 루틴 구함

        /*
        개인 루틴
         */
        List<PersonalRoutineByUserProfileGetResponse> personalRoutineGetResponses = new ArrayList<>();
        for (Routine routine : personalRoutines) {
            if (routine.getStartDate().isBefore(date) || routine.getStartDate().isEqual(date)) {
                List<String> repeatDays = routine.getRoutineRepeatDays().stream()
                        .map(rd -> rd.getRepeatDay().getLabel()).toList();

                Boolean isCompletion = completionRoutineService.getIsCompletionRoutine(user, routine, date);

                personalRoutineGetResponses.add(PersonalRoutineByUserProfileGetResponse.of(
                        routine, repeatDays, isCompletion
                ));
            }
        }

        /*
        그룹 루틴
         */
        List<GroupRoutinesGetResponse> groupRoutinesGetResponses = new ArrayList<>();
        Set<Group> userGroups = user.getGroupMembers().stream().map(GroupMember::getGroup).collect(Collectors.toSet());
        for (Group userGroup : userGroups) {
            if (!userGroup.getIsPublic()) {
                continue;
            }
            List<Routine> routines = userGroup.getGroupRoutines().stream().map(GroupRoutine::getRoutine).toList();
            List<Routine> filterRoutines = routineRepeatDayService.filterRoutinesByDay(routines, date.getDayOfWeek());

            if (filterRoutines.isEmpty()) {
                continue;
            }

            List<GroupRoutineInfo> groupRoutineInfos = filterRoutines.stream()
                    .map(routine -> GroupRoutineInfo.of(routine,
                            completionRoutineService.getIsCompletionRoutine(user, routine, date)))
                    .toList();

            groupRoutinesGetResponses.add(GroupRoutinesGetResponse.of(userGroup, groupRoutineInfos));
        }

        return RoutinesByUserProfileGetResponse.of(user, personalRoutineGetResponses, groupRoutinesGetResponses,
                userEmotionService.getUserEmotionByDate(user, date));
    }

    public void createRoutine(User user, RoutineCreateRequest request) {
        LocalDate startDate = LocalDate.parse(request.startDate(), DATE_FORMATTER);
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("루틴 시작일은 과거일 수 없습니다!");
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
            throw new IllegalArgumentException("자신의 루틴만 수정할 수 있습니다!");
        }

        if (!routine.getIsPersonal()) {
            throw new IllegalArgumentException("개인 루틴만 수정할 수 있습니다!");
        }

        routine.updateByPersonal(request.routineTitle(), getCategoryByLabel(request.routineCategory()),
                request.isAlarmEnabled(),
                LocalDate.parse(request.startDate(), DATE_FORMATTER));

        routineRepeatDayService.updateRoutineRepeatDay(routine, request.repeatDays());
    }

    public void deleteRoutine(User user, Long routineId) {
        Routine routine = getRoutineOrException(routineId);

        if (!routine.getCreatedUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("자신의 루틴만 삭제할 수 있습니다!");
        }

        if (!routine.getIsPersonal()) {
            throw new IllegalArgumentException("개인 루틴만 삭제할 수 있습니다!");
        }

        routineRepository.delete(routine);
    }

    public void setRoutineCompletionStatus(User user, Long routineId, CompletionRoutineRequest request) {
        Routine routine = getRoutineOrException(routineId);

        LocalDate routineDate = LocalDate.parse(request.date(), DATE_FORMATTER);

        if (!routine.getIsPersonal()) { // 그룹 루틴인가?
            if (!groupRoutineService.userIsGroupIn(routine, user)) {
                throw new IllegalArgumentException("유저가 해당 루틴이 있는 그룹의 멤버가 아닙니다!");
            }
        }

        if (routineDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("미래의 루틴을 완료할 수 없습니다!");
        }

        String day = routineDate.format(DateTimeFormatter.ofPattern("E", KOREAN));

        if (routine.getRoutineRepeatDays().stream()
                .noneMatch(routineRepeatDay -> routineRepeatDay.getRepeatDay().getLabel().equals(day))) {
            throw new IllegalArgumentException("해당 날짜에 루틴이 반복되지 않습니다.");
        }

        completionRoutineService.setCompletionRoutineStatus(user, routine, routineDate);
    }

    public Routine getRoutineOrException(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(() ->
                new IllegalArgumentException("해당 ID를 가진 루틴이 없습니다."));
    }

    public void createGroupRoutine(User user, Group group, GroupRoutineCreateRequest request) {
        Routine routine = Routine.builder()
                .createdUserId(user.getUserId())
                .routineTitle(request.routineTitle())
                .routineCategory(getCategoryByLabel(request.routineCategory()))
                .isAlarmEnabled(true)
                .isPersonal(false)
                .startDate(LocalDate.now())
                .build();

        routineRepository.save(routine);

        routineRepeatDayService.createRoutineRepeatDay(routine, request.repeatDays());

        groupRoutineService.recordGroupRoutine(group, routine);
    }

    public void updateGroupRoutine(Group group, Long routineId, GroupRoutineUpdateRequest request) {
        Routine routine = getRoutineOrException(routineId);

        if (!groupRoutineService.isGroupRoutine(group, routine)) {
            throw new IllegalArgumentException("해당 루틴이 해당 그룹의 루틴이 아닙니다!");
        }

        routine.updateByPublic(request.routineTitle(), getCategoryByLabel(request.routineCategory()));

        routineRepeatDayService.updateRoutineRepeatDay(routine, request.repeatDays());
    }

    public void deleteGroupRoutine(Group group, Long routineId) {
        Routine routine = getRoutineOrException(routineId);

        if (!groupRoutineService.isGroupRoutine(group, routine)) {
            throw new IllegalArgumentException("해당 루틴이 해당 그룹의 루틴이 아닙니다!");
        }

        routineRepository.delete(routine);
    }

    public List<RoutineCompletionInfo> getUserRoutineCompletionStatisticsByMonth(User user, YearMonth yearMonth,
                                                                                 List<CompletionRoutine> completionRoutines) {
        // 유저가 완료한 월간 루틴 현황을 담음 <일자, 해당 일자에 완료한 루틴 갯수>
        Map<Integer, Integer> completionRoutineStatistics = completionRoutines.stream()
                .collect(Collectors.toMap(
                        cr -> cr.getCompletionDate().getDayOfMonth(),
                        cr -> 1,
                        Integer::sum
                ));

        Map<Integer, Integer> routineStatistics = new HashMap<>(); // 유저의 월간 루틴 존재 현황을 담음 <일자, 해당 일자 루틴 갯수>

        /*
        해당 월, 해당 일에 수행 가능한 개인루틴, 그룹루틴 수 구하기
         */
        for (Integer day : completionRoutineStatistics.keySet()) {
            LocalDate date = yearMonth.atDay(day);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            int routineCount = 0;

            List<Routine> personalRoutines = new ArrayList<>(
                    routineRepeatDayService.getPersonalRoutinesByDay(user, dayOfWeek));
            Iterator<Routine> iterator = personalRoutines.iterator();

            while (iterator.hasNext()) {
                Routine routine = iterator.next();
                if (routine.getStartDate().isBefore(date) || routine.getStartDate().isEqual(date)) {
                    continue;
                }

                iterator.remove();
            }

            routineCount += personalRoutines.size();

            for (GroupMember groupMember : user.getGroupMembers()) {
                if (!groupMember.getGroupJoinDate().toLocalDate().isBefore(date) && !groupMember.getGroupJoinDate()
                        .toLocalDate().isEqual(date)) {
                    routineCount += routineRepeatDayService.filterRoutinesByDay(
                            groupMember.getGroup().getGroupRoutines().stream().map(GroupRoutine::getRoutine).toList(),
                            dayOfWeek).size();
                }
            }

            routineStatistics.put(day, routineCount);
        }

        // 루틴 완료 비율 계산 <일자, 완료 비율>
        Map<Integer, Float> completionRatios = new HashMap<>();

        for (Integer day : completionRoutineStatistics.keySet()) {
            completionRatios.put(day, (float) completionRoutineStatistics.get(day) / routineStatistics.get(day));
        }

        return completionRatios.entrySet().stream()
                .map(entry -> RoutineCompletionInfo.of(
                        entry.getKey(),
                        RoutineCompletionLevel.fromRate(entry.getValue()).getLevel()
                ))
                .sorted(Comparator.comparingInt(RoutineCompletionInfo::day)) // 날짜순 오름차순 정렬
                .toList();
    }

    private Category getCategoryByLabel(String label) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("카테고리 형식이 잘못됐습니다!"));
    }

}
