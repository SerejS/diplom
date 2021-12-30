package com.serejs.diplom.desktop.text;

import com.kursx.parser.fb2.Element;
import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.Section;
import com.serejs.diplom.desktop.text.container.Format;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.parse.file.FileParser;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

import javax.swing.text.html.HTML;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Converter {
    public List<Literature> convert(HashMap<String, Format> urls) {
        List<Literature> literatures = new LinkedList<>();

        for (String url : urls.keySet()) {
            File file = new File(Objects.requireNonNull(Converter.class.getClassLoader().getResource(url)).getFile());

            try {
                switch (urls.get(url).getType()) {
                    case EPUB -> literatures.add(fromEpub(file));
                    case FB2 -> literatures.add(fromFb2(file));
                    case CUSTOM -> literatures.add(fromCustom(file, urls.get(url)));
                    case WEB -> System.err.println("поиск из web не реализован");
                    default -> System.err.println("Тип литературы не определен: " + url);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получения информации из ресурса");
            }
        }

        return literatures;
    }

    public static Literature fromFb2(File file) throws Exception {
        FictionBook fb = new FictionBook(file);
        Map<String, String> fragments = new HashMap<>();
        List<Section> sections = fb.getBody().getSections();

        for (int i = 0; i < sections.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sections.get(i).getElements().forEach(el -> sb.append(el.getText()));
            fragments.put(String.valueOf(i+1), sb.toString());
        }

        return new Literature(fragments);
    }

    private static Literature fromEpub(File file) throws Exception {
        Book book = new EpubReader().readEpub(new FileInputStream(file));
        Map<String, String> fragments = new HashMap<>();

        for (Resource res : book.getTableOfContents().getAllUniqueResources()) {
            Document doc = Jsoup.parse(new String(res.getData(), StandardCharsets.UTF_8));

            fragments.put(doc.getElementsByTag("h2").text(), doc.body().text());
        }


        return new Literature(fragments);
    }

    public static Literature fromCustom(File file, Format format) {
        String text = FileParser.getText(file);
        Map<String, String> fragments = new HashMap<>();

        for (String p : text.split(format.getPrev())) {
            if (p.length() != 0) {
                int mid = p.indexOf(format.getMid());

                fragments.put(
                        p.substring(0, mid),
                        p.substring(mid+1).split(format.getAfter())[0]
                );
            }
        }

        return new Literature(fragments);
    }
}
