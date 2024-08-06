package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long groupMemberId;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean isGroupAlarmEnabled;

    @Column(nullable = false)
    private LocalDateTime groupJoinDate;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public GroupMember(Boolean isGroupAlarmEnabled, Group group, User user) {
        this.isGroupAlarmEnabled = isGroupAlarmEnabled;
        this.groupJoinDate = LocalDateTime.now();
        this.group = group;
        this.user = user;
    }

}
