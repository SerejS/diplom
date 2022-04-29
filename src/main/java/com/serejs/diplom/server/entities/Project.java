package com.serejs.diplom.server.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "project")
@EntityListeners(AuditingEntityListener.class)
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    @CreatedDate
    @GeneratedValue
    private Date date;

    @ManyToOne
    @JoinColumn(name = "id_view")
    private View view;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Literature> literatures;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Theme> themes;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<EngineParams> engines;

    public String getTitle() {return title;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public void setEngines(List<EngineParams> engines) {
        this.engines = engines;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", view=" + view +
                ", literatures=" + literatures +
                ", themes=" + themes +
                ", engines=" + engines +
                '}';
    }
}
