package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
