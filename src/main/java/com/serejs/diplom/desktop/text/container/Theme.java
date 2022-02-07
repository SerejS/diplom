package com.serejs.diplom.desktop.text.container;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


//Percent относительно root -> фактический
public class Theme {
    private Theme root;
    private String title;
    private double percent;
    private Set<String> keyWords;


    public Theme(Theme root, String title, double percent, Set<String> keyWords) {
        this.root = root;
        this.title = title;

        if (this.root != null) {
            keyWords.addAll(root.keyWords);
            this.percent = percent * root.percent / 100.;
        } else {
            this.percent = percent;
        }

        this.keyWords = keyWords;
    }

    public Theme getRoot() {
        return root;
    }

    public String getTitle() {
        return title;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public static List<Theme> getLeafThemes(List<Theme> themes) {
        ArrayDeque<Theme> deque = new ArrayDeque<>(themes);
        ArrayDeque<Theme> childDeque = new ArrayDeque<>();
        while (!deque.isEmpty()) {
            Theme theme = deque.pop();
            List<Theme> subThemes = deque.stream().filter(t -> t.getRoot() == theme).toList();
            if (subThemes.size() > 0) {
                subThemes.forEach(deque::push);
            } else {
                childDeque.push(theme);
            }
        }
        return childDeque.stream().distinct().toList();
    }

    public Set<String> getKeyWords() {
        return keyWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return percent == theme.percent && Objects.equals(root, theme.root) && Objects.equals(title, theme.title) && Objects.equals(keyWords, theme.keyWords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, title, percent, keyWords);
    }
}
