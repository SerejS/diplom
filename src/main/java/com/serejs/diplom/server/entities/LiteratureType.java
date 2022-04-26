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

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<EngineParams> params;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<Literature> literatures;

    @ManyToMany(mappedBy = "types", cascade = CascadeType.ALL)
    Set<Theme> themes;

    @ManyToOne
    View view;

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

    public void setView(View view) {
        this.view = view;
    }
}
