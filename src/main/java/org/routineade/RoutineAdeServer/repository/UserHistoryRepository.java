package org.routineade.RoutineAdeServer.repository;

import java.util.Optional;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    @Query("SELECT uh FROM UserHistory uh WHERE uh.user = :user AND uh.createdDate LIKE CONCAT(:createdDate, '%')")
    Optional<UserHistory> findByUserAndCreatedDate(User user, String createdDate);
}
