package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.routineade.RoutineAdeServer.domain.common.Category;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long routineId;

    @Column(columnDefinition = "varchar(15)", nullable = false)
    private String routineTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category routineCategory;

    @Column(nullable = false)
    private Boolean isAlarmEnabled;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean isPersonal;

    @Column(nullable = false)
    private Long createdUserId;

    @OneToMany(mappedBy = "routine", cascade = ALL)
    private List<CompletionRoutine> completionRoutines = new ArrayList<>();

    @OneToMany(mappedBy = "routine", cascade = ALL)
    private List<GroupRoutine> groupRoutines = new ArrayList<>();

    @OneToMany(mappedBy = "routine", cascade = ALL)
    private List<RoutineRepeatDay> routineRepeatDays = new ArrayList<>();

    @Builder
    public Routine(String routineTitle, Category routineCategory, Boolean isAlarmEnabled, LocalDate startDate,
                   Boolean isPersonal, Long createdUserId) {
        this.routineTitle = routineTitle;
        this.routineCategory = routineCategory;
        this.isAlarmEnabled = isAlarmEnabled;
        this.startDate = startDate;
        this.isPersonal = isPersonal;
        this.createdUserId = createdUserId;
    }

    public void update(String routineTitle, Category routineCategory, Boolean isAlarmEnabled, LocalDate startDate) {
        this.routineTitle = routineTitle;
        this.routineCategory = routineCategory;
        this.isAlarmEnabled = isAlarmEnabled;
        this.startDate = startDate;
    }

}
