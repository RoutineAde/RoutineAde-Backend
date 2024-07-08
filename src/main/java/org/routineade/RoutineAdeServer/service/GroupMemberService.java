package org.routineade.RoutineAdeServer.service;

import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupMember;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    public void createGroupMember(Group group, User user) {
        GroupMember groupMember = GroupMember.builder()
                .group(group)
                .user(user)
                .build();

        groupMemberRepository.save(groupMember);
    }

}
