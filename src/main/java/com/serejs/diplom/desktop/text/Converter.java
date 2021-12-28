package com.serejs.diplom.desktop.text;

import com.serejs.diplom.desktop.text.container.Format;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.parse.file.FileParser;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Converter {
    public List<Literature> convert(HashMap<String, Format> urls) {
        List<Literature> literatures = new LinkedList<>();

        for (String url : urls.keySet()) {
            File file = new File(Objects.requireNonNull(Converter.class.getClassLoader().getResource(url)).getFile());

            switch (urls.get(url).getType()) {
                case EPUB -> literatures.add(fromEpub(file));
                case FB2 -> literatures.add(fromFb2(file));
                case CUSTOM -> literatures.add(fromCustom(file, urls.get(url)));
                case WEB -> System.err.println("поиск из web не реализован");
                default -> System.err.println("Тип литературы не определен: " + url);
            }
        }

        return literatures;
    }

    private Literature fromFb2(File file) {
        return new Literature();
    }

    private Literature fromEpub(File file) {
        return new Literature();
    }

    private Literature fromCustom(File file, Format format) {
        return new Literature();
    }
}
