package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCategoryStatistics {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long userStatisticsId;

    @Column(nullable = false)
    private LocalDate statisticsDate;

    @Column(columnDefinition = "int default 0", nullable = false)
    private int allRoutineCount;

    @Column(columnDefinition = "int default 0", nullable = false)
    private int dailyRoutineCount;

    @Column(columnDefinition = "int default 0", nullable = false)
    private int healthRoutineCount;

    @Column(columnDefinition = "int default 0", nullable = false)
    private int careRoutineCount;

    @Column(columnDefinition = "int default 0", nullable = false)
    private int developmentRoutineCount;

    @Column(columnDefinition = "int default 0", nullable = false)
    private int otherRoutineCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserCategoryStatistics(LocalDate statisticsDate, User user) {
        this.statisticsDate = statisticsDate;
        this.user = user;
    }
}
