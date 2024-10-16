package org.routineade.RoutineAdeServer.repository;

import java.util.Optional;
import org.routineade.RoutineAdeServer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Boolean existsByNickname(String nickname);
}
