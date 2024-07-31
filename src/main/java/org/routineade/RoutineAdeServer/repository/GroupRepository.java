package org.routineade.RoutineAdeServer.repository;

import java.util.List;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g WHERE g.groupTitle LIKE CONCAT('%', :keyword, '%')")
    List<Group> findByKeyword(String keyword);

    @Query("SELECT g FROM Group g WHERE :groupCategory IS NULL OR g.groupCategory = :groupCategory")
    List<Group> findByGroupCategory(Category groupCategory);

}
