package com.serejs.diplom.server.entities;

import javax.persistence.*;

@Entity
@Table(name = "literature_format")
public class LiteratureFormat {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 20)
    private String prev;
    @Column(length = 20)
    private String mid;
    @Column(length = 20)
    private String after;

    @OneToOne
    @JoinColumn(name = "litfile", referencedColumnName = "id")
    private Literature literature;


    public Literature getLiterature() {
        return literature;
    }

    public void setLiterature(Literature literature) {
        this.literature = literature;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String firstSet) {
        this.prev = firstSet;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
