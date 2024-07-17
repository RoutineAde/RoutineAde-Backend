package org.routineade.RoutineAdeServer.repository;

import java.time.LocalDate;
import org.routineade.RoutineAdeServer.domain.CompletionRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRoutineRepository extends JpaRepository<CompletionRoutine, Long> {

    Boolean existsByUserAndRoutineAndCompletionDate(User user, Routine routine, LocalDate CompletionDate);

    void deleteByUserAndRoutineAndCompletionDate(User user, Routine routine, LocalDate CompletionDate);

    Boolean existsByUserAndRoutine(User user, Routine routine);

}
