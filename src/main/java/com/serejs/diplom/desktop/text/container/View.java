package com.serejs.diplom.desktop.text.container;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class View {
    private Long id;
    private final String title;

    @Override
    public String toString() {
        return title;
    }
}
