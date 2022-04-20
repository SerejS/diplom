package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.analyze.Analyzer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.*;


@EqualsAndHashCode
@Data
//Percent относительно root -> фактический
public class Theme {
    private Long id;
    private Theme root;
    private String title;
    private double percent;
    private String textKeyNGrams;
    private Set<String> keyNGrams;
    private Set<LiteratureType> types;

    public Theme(Theme root, String title, double percent, String textKeyNGrams, Set<LiteratureType> types) {
        this.root = root;
        this.title = title;
        this.textKeyNGrams = textKeyNGrams;
        this.keyNGrams = new HashSet<>(Analyzer.parseNGrams(textKeyNGrams));
        this.types = types;

        if (this.root != null) {
            this.types.addAll(root.types);
            this.keyNGrams.addAll(root.keyNGrams);
            this.percent = percent * root.percent / 100.;
        } else {
            this.percent = percent / 100;
        }
    }


    public Theme(Long id, Theme root, String title, double percent, String textKeyNGrams, Set<LiteratureType> types) {

        this(root, title, percent, textKeyNGrams, types);
    }

    public void setKeyNGrams(String textKeyNGrams) {
        this.textKeyNGrams = textKeyNGrams;
        this.keyNGrams = new HashSet<>(Analyzer.parseNGrams(textKeyNGrams));
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

    @Override
    public String toString() {
        return title;
    }
}
