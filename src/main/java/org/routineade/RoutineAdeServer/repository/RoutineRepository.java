package org.routineade.RoutineAdeServer.repository;

import org.routineade.RoutineAdeServer.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
