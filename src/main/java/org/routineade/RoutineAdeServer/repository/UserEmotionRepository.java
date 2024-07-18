package org.routineade.RoutineAdeServer.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.UserEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEmotionRepository extends JpaRepository<UserEmotion, Long> {

    Optional<UserEmotion> findByUserAndCreatedDate(User user, LocalDate createdDate);

}
