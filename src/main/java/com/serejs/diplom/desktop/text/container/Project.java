package com.serejs.diplom.desktop.text.container;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Project {
    private final Long id;
    @Setter
    private String title;

    @Override
    public String toString() {
        return title;
    }
}
