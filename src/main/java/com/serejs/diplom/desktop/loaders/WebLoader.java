package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Literature;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;

import static com.serejs.diplom.desktop.utils.AttachmentParser.xmlAttachments;


public class WebLoader extends AbstractLoader {

    @Override
    public void load(Literature literature) throws IOException {
        var uri = literature.getUri();
        fragments = new HashMap<>();

        //Получение содержимого сайта в виде документа
        Document doc = Jsoup.connect(uri.toString()).get();

        //Выделение содержимого текста главных заголовков как фрагментов
        var name = uri.toString();
        var content = new StringBuilder();
        for (Element el : doc.getAllElements()) {
            //Если нечался новый раздел, сохраняем не пустой предыдущий
            if (el.tagName().equals("h1")) {
                if (!content.isEmpty()) {
                    fragments.put(name, content.toString());
                    content.setLength(0);
                }

                name = el.tagName();
            }

            if (el.tagName().equals("p")) content.append(el.text()).append('\n');
        }
        fragments.put(name, content.toString());
        attachments.put(name, xmlAttachments(doc));
    }
}
