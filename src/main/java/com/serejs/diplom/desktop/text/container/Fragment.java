package com.serejs.diplom.desktop.text.container;

public record Fragment(String content, Theme theme, boolean main) {
    public int countWords() {
        return content.split("\\s").length;
    }
}
