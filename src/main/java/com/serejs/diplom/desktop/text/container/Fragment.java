package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.analyze.Analyzer;


public class Fragment {
    private final String content;
    private Theme theme;
    private final long keyWordsQty;
    private final long wordsQty;
    private final long length;

    public Fragment(String content, Theme theme) {
        this.content = content;
        this.theme = theme;
        this.wordsQty = this.content.split("\\s").length;
        this.length = this.content.length();
        this.keyWordsQty = Analyzer.countKeyWords(this.content, theme.getKeyNGrams());
    }

    public float getConcentration() {
        return (float) this.keyWordsQty / (float) this.wordsQty;
    }

    public long countWords() {
        return this.wordsQty;
    }


    public long countKeywords() {
        return keyWordsQty;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public String getContent() {
        return this.content;
    }

    public long length() {
        return this.length;
    }
}