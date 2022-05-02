package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.Literature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiteratureRepository extends JpaRepository<Literature, Long> {
}
