package com.serejs.diplom.desktop.text.container;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Project {
    private Long id;
    private String title;
    private View view;

    @Override
    public String toString() {
        return title;
    }
}
