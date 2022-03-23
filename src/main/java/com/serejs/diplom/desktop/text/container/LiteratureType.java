package com.serejs.diplom.desktop.text.container;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LiteratureType {
    private final String title;
    private final boolean main;

    @Override
    public String toString() {
        return title;
    }
}
