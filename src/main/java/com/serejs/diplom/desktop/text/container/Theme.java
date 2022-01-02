package com.serejs.diplom.desktop.text.container;

import java.util.Set;

public record Theme(Theme root, String title, byte percent, Set<String> keyWords) {

    public Theme getRoot() {
        return root;
    }

    public byte getPercent() {
        return percent;
    }

    public Set<String> getKeyWords() {
        return keyWords;
    }

    public String getTitle() {
        return title;
    }
}
