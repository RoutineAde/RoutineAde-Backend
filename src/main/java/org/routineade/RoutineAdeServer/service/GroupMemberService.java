package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @Transactional(readOnly = true)
    public boolean isNotMember(Group group, User user) {
        return !groupMemberRepository.existsByGroupAndUser(group, user);
    }

    @Transactional(readOnly = true)
    public Integer getJoinDate(Group group, User user) {
        GroupMember groupMember = getGroupMemberOrException(group, user);
        return (int) Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), groupMember.getGroupJoinDate())) + 1;
    }

    private GroupMember getGroupMemberOrException(Group group, User user) {
        return groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 해당 그룹의 멤버가 아닙니다!"));
    }

}
