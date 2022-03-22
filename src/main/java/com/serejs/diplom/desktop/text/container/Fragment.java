package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.analyze.Analyzer;
import lombok.Getter;

@Getter
public class Fragment {
    private final String content;
    private final Theme theme;
    private final LiteratureType type;
    private final long keyWordsQty;
    private final long countWords;
    private final long length;

    public Fragment(String content, Theme theme, LiteratureType type) {
        this.content = content;
        this.theme = theme;
        this.type = type;
        this.countWords = this.content.split("\\s").length;
        this.length = this.content.length();
        this.keyWordsQty = Analyzer.countKeyNGrams(this.content, theme.getKeyNGrams());
    }

    public float getConcentration() {
        return (float) this.keyWordsQty / (float) this.countWords;
    }
}