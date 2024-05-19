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
public class UserHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long userHistoryId;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String finishedRoutine;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String unfinishedRoutine;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String finishedGroupRoutine;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String unfinishedGroupRoutine;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private Mood dailyMood;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserHistory(String finishedRoutine, String unfinishedRoutine, String finishedGroupRoutine,
                       String unfinishedGroupRoutine, Mood dailyMood, User user) {
        this.finishedRoutine = finishedRoutine;
        this.unfinishedRoutine = unfinishedRoutine;
        this.finishedGroupRoutine = finishedGroupRoutine;
        this.unfinishedGroupRoutine = unfinishedGroupRoutine;
        this.dailyMood = dailyMood;
        this.user = user;
    }
}
