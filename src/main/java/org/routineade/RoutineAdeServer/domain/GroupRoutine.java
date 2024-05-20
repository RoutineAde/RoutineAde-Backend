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
import org.routineade.RoutineAdeServer.domain.common.Category;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoutine {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long groupRoutineId;

    @Column(columnDefinition = "varchar(15)", nullable = false)
    private String GroupRoutineTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category GroupRoutineCategory;

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
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Builder
    public GroupRoutine(String groupRoutineTitle, Category groupRoutineCategory, Boolean repeatMon, Boolean repeatTue,
                        Boolean repeatWed, Boolean repeatThu, Boolean repeatFri, Boolean repeatSat, Boolean repeatSun,
                        Group group) {
        GroupRoutineTitle = groupRoutineTitle;
        GroupRoutineCategory = groupRoutineCategory;
        this.repeatMon = repeatMon;
        this.repeatTue = repeatTue;
        this.repeatWed = repeatWed;
        this.repeatThu = repeatThu;
        this.repeatFri = repeatFri;
        this.repeatSat = repeatSat;
        this.repeatSun = repeatSun;
        this.group = group;
    }

}
