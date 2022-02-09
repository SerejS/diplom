package com.serejs.diplom.desktop.loaders;

import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.Section;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

public class Fb2Loader implements ContentLoader {
    /**
     * Функция получение глав книги из файла fb2
     *
     * @param uri Файл fb2
     * @return Получение глав книги
     */
    @Override
    public Map<String, String> load(URI uri) throws ParserConfigurationException, IOException, SAXException {
        FictionBook fb = new FictionBook(new File(uri.getPath()));
        Map<String, String> fragments = new LinkedHashMap<>();

        //Разделы книжки
        ArrayDeque<Section> sectionDeque = new ArrayDeque<>(fb.getBody().getSections());
        List<Section> sections = new LinkedList<>();
        while (!sectionDeque.isEmpty()) {
            Section s = sectionDeque.pollFirst();
            if (s.getSections().isEmpty()) sections.add(s);
            else s.getSections().forEach(sectionDeque::addFirst);
        }


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
}
