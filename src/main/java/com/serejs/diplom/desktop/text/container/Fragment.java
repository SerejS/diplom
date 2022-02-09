package com.serejs.diplom.desktop.text.container;

public record Fragment(String content, Theme theme) {
    public int countWords() {
        return this.content.split("\\s").length;
    }
}
