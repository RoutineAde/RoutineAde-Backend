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
    public boolean isMember(Group group, User user) {
        return groupMemberRepository.existsByGroupAndUser(group, user);
    }

    @Transactional(readOnly = true)
    public Integer getJoinDate(Group group, User user) {
        GroupMember groupMember = getGroupMemberOrException(group, user);
        return (int) Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), groupMember.getGroupJoinDate())) + 1;
    }

    public void joinGroup(Group group, User user) {
        if (isMember(group, user)) {
            throw new IllegalArgumentException("해당 유저가 이미 해당 그룹에 가입되어 있습니다!");
        }

        groupMemberRepository.save(
                GroupMember.builder()
                        .group(group)
                        .user(user)
                        .build()
        );
    }

    public void unJoinGroup(Group group, User user) {
        if (!isMember(group, user)) {
            throw new IllegalArgumentException("해당 유저가 해당 그룹에 가입되어 있지 않습니다!");
        }
        groupMemberRepository.deleteByGroupAndUser(group, user);
    }

    @Transactional(readOnly = true)
    public Boolean isUserGroupAlarmEnabled(User user, Group group) {
        GroupMember groupMember = getGroupMemberOrException(group, user);
        return groupMember.getIsGroupAlarmEnabled();
    }

    public void updateGroupAlarm(Group group, User user) {
        GroupMember groupMember = getGroupMemberOrException(group, user);
        groupMember.updateAlarmEnabled();
    }

    private GroupMember getGroupMemberOrException(Group group, User user) {
        return groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 해당 그룹의 멤버가 아닙니다!"));
    }

}
