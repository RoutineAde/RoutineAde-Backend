package org.routineade.RoutineAdeServer.repository;

import org.routineade.RoutineAdeServer.domain.RoutineRepeatDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepeatDayRepository extends JpaRepository<RoutineRepeatDay, Long> {
    
}
