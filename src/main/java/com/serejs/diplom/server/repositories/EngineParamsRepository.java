package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.EngineParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EngineParamsRepository extends JpaRepository<EngineParams, Long> {
    @Query(value = """
        SELECT ep
        FROM EngineParams ep
        WHERE ep.project.id=:id_project
    """)
    List<EngineParams> findEngineParamsByProjectID(@Param("id_project") Long projectId);
}
