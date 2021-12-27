package com.serejs.diplom.desktop.text;

import com.serejs.diplom.desktop.text.container.Format;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.parse.file.FileParser;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;

public class Converter {
    public List<Literature> convert(HashMap<String, Format> urls) {
        List<Literature> literatures = new LinkedList<>();

        for (String url : urls.keySet()) {
            String text = FileParser.getText(url);

            switch (urls.get(url).getType()) {
                case EPUB -> literatures.add(fromEpub(text));
                case FB2 -> literatures.add(fromFb2(text));
                case CUSTOM -> literatures.add(fromCustom(text, urls.get(url)));
                case WEB -> System.err.println("поиск из web не реализован");
                default -> System.err.println("Тип литературы не определен: " + url);
            }
        }

        return literatures;
    }

    private Literature fromFb2(String text) {
        return new Literature();
    }

    private Literature fromEpub(String text) {
        return new Literature();
    }

    private Literature fromCustom(String text, Format format) {
        return new Literature();
    }
}
