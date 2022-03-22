package com.serejs.diplom.desktop.text.container;

import java.util.*;


//Percent относительно root -> фактический
public class Theme {
    private Theme root;
    private String title;
    private double percent;
    private Set<String> keyNGrams;
    private Set<LiteratureType> types;

    public Theme(Theme root, String title, double percent, Set<String> keyNGrams, Set<LiteratureType> types) {
        this.root = root;
        this.title = title;
        this.keyNGrams = new HashSet<>(keyNGrams);
        this.types = types;

        if (this.root != null) {
            this.types.addAll(root.types);
            this.keyNGrams.addAll(root.keyNGrams);
            this.percent = percent * root.percent / 100.;
        } else {
            this.percent = percent / 100;
        }
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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void setRoot(Theme root) {
        this.root = root;
    }

    public void setKeyNGrams(Set<String> keyNGrams) {
        this.keyNGrams = new HashSet<>(keyNGrams);
        if (root != null) this.keyNGrams.addAll(root.keyNGrams);
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

    public Set<String> getKeyNGrams() {
        return this.keyNGrams;
    }

    public Set<LiteratureType> getTypes() {
        return types;
    }

    public void setTypes(Set<LiteratureType> types) {
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return percent == theme.percent && Objects.equals(root, theme.root) && Objects.equals(title, theme.title) && Objects.equals(keyNGrams, theme.keyNGrams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, title, percent, keyNGrams);
    }

    @Override
    public String toString() {
        return title;
    }
}
