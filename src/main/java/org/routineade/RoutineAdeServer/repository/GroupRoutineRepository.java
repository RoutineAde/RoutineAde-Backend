package org.routineade.RoutineAdeServer.repository;

import java.util.Optional;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRoutineRepository extends JpaRepository<GroupRoutine, Long> {

    Optional<GroupRoutine> findByRoutine(Routine routine);

    Boolean existsByGroupAndRoutine(Group group, Routine routine);
}
