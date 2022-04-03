package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query(value = """
        SELECT t
        FROM Theme t
        WHERE t.project.id=:id_project
    """)
    List<Theme> findThemesByProjectId(@Param("id_project") Long projectId);
}
