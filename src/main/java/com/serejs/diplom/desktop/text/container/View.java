package com.serejs.diplom.desktop.text.container;

public class View {
    private final Long id;
    private final String title;

    public View(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return title;
    }
}
