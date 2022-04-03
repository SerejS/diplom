package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
