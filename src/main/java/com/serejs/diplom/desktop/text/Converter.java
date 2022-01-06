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
    public static HashSet<Literature> convert(List<Source> sources) throws Exception {
        HashSet<Literature> literatures = new HashSet<>();

        for (Source source : sources) {
            File file = new File(Objects.requireNonNull(Converter.class.getClassLoader().getResource(source.url())).getFile());
            String fileName = file.getName();

            //Получение литературы с помощью функций каждого своего формата
            switch (source.type()) {
                case EPUB -> literatures.add(new Literature(fileName, fromEpub(file), source.main()));
                case FB2 -> literatures.add(new Literature(fileName, fromFb2(file), source.main()));
                case CUSTOM -> literatures.add(new Literature(fileName, fromCustom(file, source.format()), source.main()));
                default -> System.err.println("Тип литературы не определен: " + source.url());
            }
        }

        return literatures;
    }


    /**
     * Функция получение глав книги из файла fb2
     * @param file Файл fb2
     * @return Получение глав книги
     */
    private static Map<String, String> fromFb2(File file) throws Exception {
        FictionBook fb = new FictionBook(file);
        Map<String, String> fragments = new LinkedHashMap<>();
        //Разделы книжки
        List<Section> sections = fb.getBody().getSections();

        for (int i = 0; i < sections.size(); i++) {
            //Получение названия главы. Берется title из xml
            String title = sections.get(i).getTitleString(" - ", "");
            //Если названия главы нет, то пишется ее номер
            if (title.isEmpty()) title = String.valueOf(i + 1);

            //Собирание текста главы из всего текста в тегах
            StringBuilder sb = new StringBuilder();
            sections.get(i).getElements().forEach(el -> sb.append(el.getText()));

            fragments.put(title, sb.toString());
        }

        return fragments;
    }


    /**
     * Функция получение глав книги из файла epub
     * @param file Файл epub
     * @return Получение глав книги
     */
    private static Map<String, String> fromEpub(File file) throws IOException {
        //Преобразование файла в книгу java
        Book book = new EpubReader().readEpub(new FileInputStream(file));
        Map<String, String> fragments = new HashMap<>();

        //Получение таблицы глав и добавление их в мапу
        for (Resource res : book.getTableOfContents().getAllUniqueResources()) {
            Document doc = Jsoup.parse(new String(res.getData(), StandardCharsets.UTF_8));

            fragments.put(doc.getElementsByTag("h2").text(), doc.body().text());
        }

        return fragments;
    }

    /**
     * Функция получение глав книги из файла пользовательского формата
     * @param file Файл epub
     * @param format Формат книги: разделители до названия главы, после и после основного текста
     * @return Получение глав книги
     */
    private static Map<String, String> fromCustom(File file, Format format) {
        //Получение текста из файла
        String text = FileParser.getText(file);
        Map<String, String> fragments = new HashMap<>();

        //Разделение текста на главы (разделитель до названия главы)
        for (String p : text.split(format.getPrev())) {
            //Если глава не пустая
            if (p.length() != 0) {
                //Получение символа между названием главы и содержанием
                int mid = p.indexOf(format.getMid());

                //Добавление названия главы и содержания в мапу
                fragments.put(
                        p.substring(0, mid),
                        p.substring(mid + 1).split(format.getAfter())[0]
                );
            }
        }

        return fragments;
    }
}
