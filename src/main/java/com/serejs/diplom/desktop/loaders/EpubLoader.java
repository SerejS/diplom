package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Fragment;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EpubLoader implements ContentLoader {
    /**
     * Функция получение глав книги из файла epub
     * @param uri источник epub
     * @return Получение глав книги
     */
    @Override
    public Map<String, String> load(URI uri) throws IOException {
        //Преобразование файла в книгу java
        Book book = new EpubReader().readEpub(new FileInputStream(String.valueOf(uri)));
        Map<String, String> fragments = new HashMap<>();

        //Получение таблицы глав и добавление их в мапу
        for (Resource res : book.getTableOfContents().getAllUniqueResources()) {
            Document doc = Jsoup.parse(new String(res.getData(), StandardCharsets.UTF_8));

            fragments.put(doc.getElementsByTag("h2").text(), doc.body().text());
        }

        return fragments;
    }
}
