package org.routineade.RoutineAdeServer.repository;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query("SELECT r FROM Routine r WHERE r.user = :user AND r.repeatMon = true")
    List<Routine> findByUserAndMonDay(User user);

    @Query("SELECT r FROM Routine r WHERE r.user = :user AND r.repeatTue = true")
    List<Routine> findByUserAndTueDay(User user);

    @Query("SELECT r FROM Routine r WHERE r.user = :user AND r.repeatWed = true")
    List<Routine> findByUserAndWedDay(User user);

    @Query("SELECT r FROM Routine r WHERE r.user = :user AND r.repeatThu = true")
    List<Routine> findByUserAndThuDay(User user);

    @Query("SELECT r FROM Routine r WHERE r.user = :user AND r.repeatFri = true")
    List<Routine> findByUserAndFriDay(User user);

    @Query("SELECT r FROM Routine r WHERE r.user = :user AND r.repeatSat = true")
    List<Routine> findByUserAndSatDay(User user);

    @Query("SELECT r FROM Routine r WHERE r.user = :user AND r.repeatSun = true")
    List<Routine> findByUserAndSunDay(User user);

}
