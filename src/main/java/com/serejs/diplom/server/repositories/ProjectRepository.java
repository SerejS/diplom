package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = """
        SELECT p
        FROM Project p
        WHERE p.view.id=:id_view
    """)
    List<Project> findProjectByViewId(@Param("id_view") Long viewId);
}
