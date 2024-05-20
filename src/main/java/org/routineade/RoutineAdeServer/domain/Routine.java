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
    private String startDate;

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
    public Routine(String routineTitle, Category routineCategory, Boolean isAlarmEnabled, String startDate,
                   boolean[] isRepeatDays, User user) {
        this.routineTitle = routineTitle;
        this.routineCategory = routineCategory;
        this.isAlarmEnabled = isAlarmEnabled;
        this.startDate = startDate;
        this.repeatMon = isRepeatDays[0];
        this.repeatTue = isRepeatDays[1];
        this.repeatWed = isRepeatDays[2];
        this.repeatThu = isRepeatDays[3];
        this.repeatFri = isRepeatDays[4];
        this.repeatSat = isRepeatDays[5];
        this.repeatSun = isRepeatDays[6];
        this.user = user;
    }

    public void updateValue(String routineTitle, Category routineCategory, Boolean isAlarmEnabled,
                            boolean[] isRepeatDays) {
        this.routineTitle = routineTitle;
        this.routineCategory = routineCategory;
        this.isAlarmEnabled = isAlarmEnabled;
        this.repeatMon = isRepeatDays[0];
        this.repeatTue = isRepeatDays[1];
        this.repeatWed = isRepeatDays[2];
        this.repeatThu = isRepeatDays[3];
        this.repeatFri = isRepeatDays[4];
        this.repeatSat = isRepeatDays[5];
        this.repeatSun = isRepeatDays[6];
    }
}
