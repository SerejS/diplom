package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.LiteratureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LiteratureTypeRepository extends JpaRepository<LiteratureType, Long> {
    @Query(value = """
        SELECT lt
        FROM LiteratureType lt
        WHERE lt.view.id=:id_view
    """)
    List<LiteratureType> findLiteratureTypeByViewId(@Param("id_view") Long viewId);
}
