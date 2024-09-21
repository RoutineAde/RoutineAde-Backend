package org.routineade.RoutineAdeServer.repository;

import org.routineade.RoutineAdeServer.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query(value = "SELECT COUNT(r.routineId) FROM Routine r "
            + "WHERE r.createdUserId = :userId "
            + "AND r.isPersonal = true")
    Long countByUserPersonal(Long userId);
}
