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
import org.routineade.RoutineAdeServer.domain.common.Category;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long routineId;

    @Column(columnDefinition = "varchar(15)", nullable = false)
    private String routineTitle;

    @Column(nullable = false)
    private Category routineCategory;

    @Column(nullable = false)
    private Boolean isAlarmEnabled;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean repeatMon;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean repeatTue;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean repeatWed;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean repeatThu;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean repeatFri;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean repeatSat;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean repeatSun;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Routine(String routineTitle, Category routineCategory, Boolean isAlarmEnabled, Boolean repeatMon,
                   Boolean repeatTue, Boolean repeatWed, Boolean repeatThu, Boolean repeatFri, Boolean repeatSat,
                   Boolean repeatSun, User user) {
        this.routineTitle = routineTitle;
        this.routineCategory = routineCategory;
        this.isAlarmEnabled = isAlarmEnabled;
        this.repeatMon = repeatMon;
        this.repeatTue = repeatTue;
        this.repeatWed = repeatWed;
        this.repeatThu = repeatThu;
        this.repeatFri = repeatFri;
        this.repeatSat = repeatSat;
        this.repeatSun = repeatSun;
        this.user = user;
    }
}
