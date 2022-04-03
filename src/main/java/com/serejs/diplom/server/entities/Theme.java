package com.serejs.diplom.server.entities;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "theme")
public class Theme {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 45)
    private String title;
    private Double percent;

    @ManyToOne
    @JoinColumn(name = "id_project")
    private Project project;

    @Column(columnDefinition = "TEXT")
    private String ngrams;

    @ManyToMany
    @JoinTable(
            name = "theme_types",
            joinColumns = @JoinColumn(name = "id_themes"),
            inverseJoinColumns = @JoinColumn(name = "id_types"))
    Set<LiteratureType> types;

    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    @JoinColumn(name = "root")
    private Theme root;

    public Long getRoot() {
        if (root == null) return null;

        return root.getId();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Long getProject() {
        return project.getId();
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getNgrams() {
        return ngrams;
    }

    public void setNgrams(String ngrams) {
        this.ngrams = ngrams;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
