package org.routineade.RoutineAdeServer.repository;

import org.routineade.RoutineAdeServer.domain.BanGroupMember;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanGroupMemberRepository extends JpaRepository<BanGroupMember, Long> {

    Boolean existsByGroupAndUser(Group group, User user);

}
