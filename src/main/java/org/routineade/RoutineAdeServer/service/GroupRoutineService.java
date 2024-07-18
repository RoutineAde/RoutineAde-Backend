package org.routineade.RoutineAdeServer.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    public boolean userIsGroupIn(Routine routine, User user) {
        GroupRoutine groupRoutine = getGroupRoutineOrException(routine);

        return groupRoutine.getGroup().getGroupMembers().stream()
                .map(GroupMember::getUser)
                .collect(Collectors.toSet())
                .contains(user);
    }

    private GroupRoutine getGroupRoutineOrException(Routine routine) {
        return groupRoutineRepository.findByRoutine(routine)
                .orElseThrow(() -> new RuntimeException("해당 루틴이 속한 그룹을 찾을 수 없습니다!"));
    }
}
