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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.routineade.RoutineAdeServer.domain.common.Day;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineRepeatDay {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long routineRepeatDayId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(3)", nullable = false)
    private Day repeatDay;

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    private RoutineRepeatDay(Day repeatDay, Routine routine) {
        this.repeatDay = repeatDay;
        this.routine = routine;
    }

    public static RoutineRepeatDay of(Day repeatDay, Routine routine) {
        return new RoutineRepeatDay(repeatDay, routine);
    }

}
