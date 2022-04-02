package com.serejs.diplom.server.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "literature_type")
public class LiteratureType {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;
    private boolean main;

    @OneToMany(mappedBy = "type")
    private List<EngineParams> params;

    @OneToMany(mappedBy = "type")
    private List<Literature> literatures;

    @ManyToMany(mappedBy = "types")
    Set<Theme> themes;


    public LiteratureType() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
