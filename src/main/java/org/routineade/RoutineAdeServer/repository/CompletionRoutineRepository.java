package org.routineade.RoutineAdeServer.repository;

import java.time.LocalDate;
import java.util.List;
import org.routineade.RoutineAdeServer.domain.CompletionRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRoutineRepository extends JpaRepository<CompletionRoutine, Long> {

    Boolean existsByUserAndRoutineAndCompletionDate(User user, Routine routine, LocalDate CompletionDate);

    void deleteByUserAndRoutineAndCompletionDate(User user, Routine routine, LocalDate CompletionDate);

    @Modifying
    @Query("DELETE FROM CompletionRoutine cr WHERE cr.user = :user AND cr.routine IN :routines")
    void deleteByUserAndRoutines(User user, List<Routine> routines);

}
