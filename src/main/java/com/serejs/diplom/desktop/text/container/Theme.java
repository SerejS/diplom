package com.serejs.diplom.desktop.text.container;

import javafx.util.Pair;

import java.util.*;


//Percent относительно root -> фактический
public class Theme {
    private Theme root;
    private String title;
    private double percent;
    private Map<LiteratureType, Pair<String, Set<String>>> mapKeyNGrams;

    public Theme(
            Theme root, String title, double percent,
            Map<LiteratureType, Pair<String, Set<String>>> mapKeyNGrams) {
        this.root = root;
        this.title = title;
        this.mapKeyNGrams = new HashMap<>(mapKeyNGrams);

        if (this.root != null) {
            mapKeyNGrams.putAll(root.getMapKeyNGrams());
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

    public void setMapKeyNGrams(Map<LiteratureType, Pair<String, Set<String>>> mapKeyNGrams) {
        this.mapKeyNGrams = new HashMap<>(mapKeyNGrams);
        if (root != null) this.mapKeyNGrams.putAll(root.mapKeyNGrams);
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

    public Map<LiteratureType, Pair<String, Set<String>>> getMapKeyNGrams() {
        return mapKeyNGrams;
    }

    public Set<String> getKeyNGrams(LiteratureType type) {
        try {
            return mapKeyNGrams.get(type).getValue();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return percent == theme.percent && Objects.equals(root, theme.root) && Objects.equals(title, theme.title) && Objects.equals(mapKeyNGrams, theme.mapKeyNGrams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, title, percent, mapKeyNGrams);
    }

    @Override
    public String toString() {
        return title;
    }
}
