package com.serejs.diplom.desktop.text.parse.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebScraper {
    public static String textFromSite(String siteURL)  {
        StringBuilder sb = new StringBuilder();

        Document doc;
        try {
            doc = Jsoup.connect(siteURL).get();
        } catch (IOException e) {
            return "";
        }
        doc.getElementsByTag("p").forEach((element) -> {
            if (element.text().length() > 100) {
                sb.append(element.text()).append('\n');
            }
        });
        return sb.toString();
    }
}
