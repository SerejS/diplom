package com.serejs.diplom.desktop.loaders;

import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.Section;
import com.serejs.diplom.desktop.enums.AttachmentType;
import com.serejs.diplom.desktop.text.container.Attachment;
import com.serejs.diplom.desktop.utils.AttachmentParser;
import org.jsoup.Jsoup;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Fb2Loader extends AbstractLoader {
    /**
     * Функция получение глав книги из файла fb2
     *
     * @param uri Файл fb2
     */
    @Override
    public void load(URI uri) throws ParserConfigurationException, IOException, SAXException {
        var file = new File(uri.getPath());

        FictionBook fb = new FictionBook(file);
        var doc = Jsoup.parse(file, "windows-1251");
        var xmlSections = doc.getElementsByTag("section");

        //Разделы книжки
        ArrayDeque<Section> sectionDeque = new ArrayDeque<>(fb.getBody().getSections());
        List<Section> sections = new LinkedList<>();

        while (!sectionDeque.isEmpty()) {
            Section s = sectionDeque.pollFirst();
            if (s.getSections().isEmpty()) sections.add(s);
            else s.getSections().forEach(sectionDeque::addFirst);
        }

        //Получение контента книги
        for (int i = 0; i < sections.size(); i++) {
            var section = sections.get(i);

            //Получение названия главы. Берется title из xml
            String title = section.getTitleString(" - ", "");
            //Если названия главы нет, то пишется ее номер
            if (title.isEmpty()) title = String.valueOf(i + 1);

            //Собирание текста главы из всего текста в тегах
            StringBuilder sb = new StringBuilder();
            section.getElements().forEach(el -> sb.append(el.getText()));

            fragments.put(title, sb.toString());

            //Сохранение приложений книги
            var fragmentAttachments = new HashSet<Attachment>();
            var image = section.getImage();
            if (image != null)
                fragmentAttachments.add(new Attachment(image, AttachmentType.IMAGE));

            fragmentAttachments.addAll(AttachmentParser.xmlAttachments(xmlSections.get(i)));
            attachments.put(title, fragmentAttachments);

        }
    }
}
