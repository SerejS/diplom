package com.serejs.diplom.server.repositories;

import com.serejs.diplom.server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
