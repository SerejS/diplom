package com.serejs.diplom.server.entities;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @CreatedDate
    private Date date;

    @ManyToOne
    @JoinColumn(name = "id_view")
    private View view;

    @OneToMany(mappedBy = "project")
    private List<Literature> literatures;

    @OneToMany(mappedBy = "project")
    private List<Theme> themes;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
