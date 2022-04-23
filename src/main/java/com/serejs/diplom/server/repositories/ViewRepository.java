package com.serejs.diplom.server.repositories;


import com.serejs.diplom.server.entities.User;
import com.serejs.diplom.server.entities.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewRepository extends JpaRepository<View, Long> {

    List<View> findAllByUser(User user);
}
