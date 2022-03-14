package com.serejs.diplom.desktop.text.container;

public class LiteratureType {
    private final String title;
    private final boolean main;

    public LiteratureType(String title, boolean main) {
        this.title = title;
        this.main = main;
    }

    public String getTitle() {
        return title;
    }

    public boolean getMain() {
        return main;
    }

    @Override
    public String toString() {
        return title;
    }
}
