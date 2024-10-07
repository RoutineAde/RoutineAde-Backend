package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.CompletionRoutine;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.repository.CompletionRoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompletionRoutineService {

    private final CompletionRoutineRepository completionRoutineRepository;

    public void setCompletionRoutineStatus(User user, Routine routine, LocalDate date) {
        if (getIsCompletionRoutine(user, routine, date)) {
            completionRoutineRepository.deleteByUserAndRoutineAndCompletionDate(user, routine, date);
        } else {
            CompletionRoutine completionRoutine = CompletionRoutine.builder()
                    .user(user)
                    .routine(routine)
                    .completionDate(date)
                    .build();

            completionRoutineRepository.save(completionRoutine);
        }
    }

    @Transactional(readOnly = true)
    public Boolean getIsCompletionRoutine(User user, Routine routine, LocalDate date) {
        return completionRoutineRepository.existsByUserAndRoutineAndCompletionDate(user, routine, date);
    }

    public void deleteCompletionRoutines(User user, List<Routine> completionRoutines) {
        completionRoutineRepository.deleteByUserAndRoutines(user, completionRoutines);
    }

    @Transactional(readOnly = true)
    public int getGroupCompletionRoutineCount(Group group) {
        List<Routine> routines = group.getGroupRoutines()
                .stream()
                .map(GroupRoutine::getRoutine)
                .toList();

        return completionRoutineRepository.countByRoutineIn(routines);
    }

}
