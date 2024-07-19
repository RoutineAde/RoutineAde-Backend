package org.routineade.RoutineAdeServer.repository;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupChatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChattingRepository extends JpaRepository<GroupChatting, Long> {

    List<GroupChatting> findAllByGroup(Group group);

}
