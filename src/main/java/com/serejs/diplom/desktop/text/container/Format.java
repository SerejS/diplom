package com.serejs.diplom.desktop.text.container;

public record Format(String prev, String mid, String after) {
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
