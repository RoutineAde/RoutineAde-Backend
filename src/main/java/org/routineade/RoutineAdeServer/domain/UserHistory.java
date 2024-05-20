package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.routineade.RoutineAdeServer.domain.common.BaseEntity;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long userHistoryId;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String finishedRoutine;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String finishedGroupRoutine;

    @Enumerated(EnumType.STRING)
    @Column
    private Mood dailyMood;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserHistory(String finishedRoutine, String finishedGroupRoutine, Mood dailyMood, User user) {
        this.finishedRoutine = finishedRoutine;
        this.finishedGroupRoutine = finishedGroupRoutine;
        this.dailyMood = dailyMood;
        this.user = user;
    }

    public void updateFinishedRoutine(String routineId) {
        if (finishedRoutine.contains(routineId)) {
            List<String> finishedRoutines = new java.util.ArrayList<>(
                    Arrays.stream(finishedRoutine.split(",")).toList());
            finishedRoutines.remove(routineId);
            this.finishedRoutine = String.join(",", finishedRoutines);
        } else {
            this.finishedRoutine += "," + routineId;
        }
    }

}
