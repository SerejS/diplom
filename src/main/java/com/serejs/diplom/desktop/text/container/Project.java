package com.serejs.diplom.desktop.text.container;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Project {
    private Long id;
    private String title;

    @Override
    public String toString() {
        return title;
    }
}
