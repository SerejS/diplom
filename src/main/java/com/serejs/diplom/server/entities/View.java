package com.serejs.diplom.server.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "profile_view", schema = "diplom")
public class View {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    public View() {}

    public View(String title, List<Project> projects) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
