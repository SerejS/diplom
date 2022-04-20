package com.serejs.diplom.desktop.text.container;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class View {
    private final Long id;
    private final String title;

    @Override
    public String toString() {
        return title;
    }
}
