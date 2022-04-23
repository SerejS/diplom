package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.ui.states.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LiteratureType {
    private Long id;
    private final String title;
    private final boolean main;
    private final View view;

    public LiteratureType(String title, boolean main) {
        this.id = -1L;
        this.title = title;
        this.main = main;
        this.view = State.getView();
    }

    public LiteratureType(String title, boolean main, View view) {
        this.id = -1L;
        this.title = title;
        this.main = main;
        this.view = view;
    }

    @Override
    public String toString() {
        return title;
    }
}
