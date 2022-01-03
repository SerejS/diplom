package com.serejs.diplom.desktop.text;

import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.Section;

import com.serejs.diplom.desktop.text.container.Format;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.text.parse.FileParser;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Converter {
    public static HashSet<Literature> convert(List<Source> sources) {
        HashSet<Literature> literatures = new HashSet<>();

        for (Source source : sources) {
            File file = new File(Objects.requireNonNull(Converter.class.getClassLoader().getResource(source.url())).getFile());

            try {
                switch (source.type()) {
                    case EPUB -> literatures.add(new Literature(fromEpub(file), source.main()));
                    case FB2 -> literatures.add(new Literature(fromFb2(file), source.main()));
                    case CUSTOM -> literatures.add(new Literature(fromCustom(file, source.format()), source.main()));
                    default -> System.err.println("Тип литературы не определен: " + source.url());
                }
            } catch (ConcurrentModificationException ex) {
                System.err.println("Создание литературы прошло неудачно");
            } catch (Exception ex) {
                System.err.println("Ошибка получения информации из ресурса");
            }
        }

        return literatures;
    }

    private static Map<String, String> fromFb2(File file) throws Exception {
        FictionBook fb = new FictionBook(file);
        Map<String, String> fragments = new LinkedHashMap<>();
        List<Section> sections = fb.getBody().getSections();

        for (int i = 0; i < sections.size(); i++) {
            String title = sections.get(i).getTitleString(".", ".");
            if (title.equals("")) title = String.valueOf(i + 1);

            StringBuilder sb = new StringBuilder();
            sections.get(i).getElements().forEach(el -> sb.append(el.getText()));

            fragments.put(title, sb.toString());
        }

        return fragments;
    }

    private static Map<String, String> fromEpub(File file) throws IOException {
        Book book = new EpubReader().readEpub(new FileInputStream(file));
        Map<String, String> fragments = new HashMap<>();

        for (Resource res : book.getTableOfContents().getAllUniqueResources()) {
            Document doc = Jsoup.parse(new String(res.getData(), StandardCharsets.UTF_8));

            fragments.put(doc.getElementsByTag("h2").text(), doc.body().text());
        }


        return fragments;
    }

    private static Map<String, String> fromCustom(File file, Format format) {
        String text = FileParser.getText(file);
        Map<String, String> fragments = new HashMap<>();

        for (String p : text.split(format.getPrev())) {
            if (p.length() != 0) {
                int mid = p.indexOf(format.getMid());

                fragments.put(
                        p.substring(0, mid),
                        p.substring(mid + 1).split(format.getAfter())[0]
                );
            }
        }

        return fragments;
    }
}
