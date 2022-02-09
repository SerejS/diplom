package com.serejs.diplom.desktop.loaders;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class WebLoader implements ContentLoader {

    @Override
    public Map<String, String> load(URI uri) throws IOException {
        var fragments = new HashMap<String, String>();

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

        return fragments;
    }
}
