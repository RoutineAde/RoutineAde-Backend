package org.routineade.RoutineAdeServer.repository;

import org.routineade.RoutineAdeServer.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
