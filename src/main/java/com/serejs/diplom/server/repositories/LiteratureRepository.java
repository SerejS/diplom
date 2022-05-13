package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.Literature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LiteratureRepository extends JpaRepository<Literature, Long> {
    @Query(value = """
        SELECT l
        FROM Literature l
        WHERE l.project.id=:id_project
    """)
    List<Literature> findLiteraturesByProjectId(@Param("id_project") Long projectId);
}
