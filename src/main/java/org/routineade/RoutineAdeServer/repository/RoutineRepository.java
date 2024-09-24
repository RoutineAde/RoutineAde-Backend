package org.routineade.RoutineAdeServer.repository;

import jakarta.transaction.Transactional;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query(value = "SELECT COUNT(r.routineId) FROM Routine r "
            + "WHERE r.createdUserId = :userId "
            + "AND r.isPersonal = true")
    Long countByUserPersonal(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Routine r "
            + "WHERE r.createdUserId = :userId "
            + "AND r.isPersonal = true")
    void deleteByUserPersonal(Long userId);
}
