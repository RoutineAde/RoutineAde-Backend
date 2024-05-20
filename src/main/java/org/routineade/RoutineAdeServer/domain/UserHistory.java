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
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long userHistoryId;

    @Column(nullable = false)
    private String historyDate;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String finishedRoutine;

    @Column(columnDefinition = "varchar(100) default ''", nullable = false)
    private String finishedGroupRoutine;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column
    private Mood dailyMood;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserHistory(String historyDate, String finishedRoutine, String finishedGroupRoutine, Mood dailyMood,
                       User user) {
        this.historyDate = historyDate;
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
            this.finishedRoutine += this.finishedRoutine.isBlank() ? routineId : "," + routineId;
        }
    }

}
