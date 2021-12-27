package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.text.parse.file.LiteratureType;

public record Format(LiteratureType type, String prev, String mid, String after) {
    public LiteratureType getType() {
        return type;
    }

    public String getPrev() {
        return prev;
    }

    public String getMid() {
        return mid;
    }

    public String getAfter() {
        return after;
    }
}
