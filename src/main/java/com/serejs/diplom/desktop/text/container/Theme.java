package com.serejs.diplom.desktop.text.container;

import java.util.Set;

public record Theme(Theme root, String title, byte percent, Set<String> keyWords) {
    public Theme recalculate(byte newPercent) {
        if (newPercent > 100) return root;

        return new Theme(root, title, newPercent, keyWords);
    }
}
