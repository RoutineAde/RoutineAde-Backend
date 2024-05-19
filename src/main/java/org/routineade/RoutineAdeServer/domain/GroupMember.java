package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.routineade.RoutineAdeServer.domain.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long groupMemberId;

    @Column(columnDefinition = "varchar(120)", nullable = false)
    private String content;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean isGroupAlarmEnabled;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public GroupMember(String content, Boolean isGroupAlarmEnabled, Group group, User user) {
        this.content = content;
        this.isGroupAlarmEnabled = isGroupAlarmEnabled;
        this.group = group;
        this.user = user;
    }
}
