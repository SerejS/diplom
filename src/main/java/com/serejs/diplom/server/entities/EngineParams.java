package com.serejs.diplom.server.entities;

import javax.persistence.*;

@Entity
@Table(name = "engines")
public class EngineParams {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private String cx;
    private String token;

    @ManyToOne
    @JoinColumn(name = "type")
    private LiteratureType type;


    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
