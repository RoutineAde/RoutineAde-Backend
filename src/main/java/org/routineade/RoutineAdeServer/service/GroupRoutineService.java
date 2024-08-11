package org.routineade.RoutineAdeServer.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupMember;
import org.routineade.RoutineAdeServer.domain.GroupRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.repository.GroupRoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupRoutineService {

    private final GroupRoutineRepository groupRoutineRepository;

    @Transactional(readOnly = true)
    public boolean userIsGroupIn(Routine routine, User user) {
        GroupRoutine groupRoutine = getGroupRoutineOrException(routine);

        return groupRoutine.getGroup().getGroupMembers().stream()
                .map(GroupMember::getUser)
                .collect(Collectors.toSet())
                .contains(user);
    }

    public void recordGroupRoutine(Group group, Routine routine) {
        GroupRoutine groupRoutine = GroupRoutine.builder()
                .group(group)
                .routine(routine)
                .build();

        groupRoutineRepository.save(groupRoutine);
    }

    @Transactional(readOnly = true)
    public boolean isGroupRoutine(Group group, Routine routine) {
        return groupRoutineRepository.existsByGroupAndRoutine(group, routine);
    }

    private GroupRoutine getGroupRoutineOrException(Routine routine) {
        return groupRoutineRepository.findByRoutine(routine)
                .orElseThrow(() -> new IllegalArgumentException("해당 루틴이 속한 그룹을 찾을 수 없습니다!"));
    }
}
