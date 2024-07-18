package org.routineade.RoutineAdeServer.repository;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.RoutineRepeatDay;
import org.routineade.RoutineAdeServer.domain.common.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepeatDayRepository extends JpaRepository<RoutineRepeatDay, Long> {

    @Query("SELECT rd FROM RoutineRepeatDay rd WHERE rd.routine.createdUserId = ?1 AND rd.repeatDay = ?2 AND rd.routine.isPersonal = true")
    List<RoutineRepeatDay> findByUserAndDay(Long userId, Day day);

    void deleteAllByRoutine(Routine routine);

}
