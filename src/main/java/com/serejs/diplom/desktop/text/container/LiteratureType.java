package com.serejs.diplom.desktop.text.container;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LiteratureType {
    private Long id;
    private final String title;
    private final boolean main;

    public LiteratureType(String title, boolean main) {
        this.id = -1L;
        this.title = title;
        this.main = main;
    }

    @Override
    public String toString() {
        return title;
    }
}
