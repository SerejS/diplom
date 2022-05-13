package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Literature;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.serejs.diplom.desktop.utils.AttachmentParser.xmlAttachments;

public class EpubLoader extends AbstractLoader {
    /**
     * Функция получение глав книги из файла epub
     *
     * @param literature источник epub
     */
    @Override
    public void load(Literature literature) throws IOException {
        //Преобразование файла в книгу java
        Book book = new EpubReader().readEpub(new FileInputStream(literature.getUri().getPath()));
        fragments = new HashMap<>();

        //Получение таблицы глав и добавление их в мапу
        for (Resource res : book.getTableOfContents().getAllUniqueResources()) {
            Document doc = Jsoup.parse(new String(res.getData(), StandardCharsets.UTF_8));
            var title = doc.getElementsByTag("h2").text();

            fragments.put(title, doc.body().text());
            attachments.put(title, xmlAttachments(doc));
        }
    }
}
