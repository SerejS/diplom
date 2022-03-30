package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.enums.AttachmentType;
import com.serejs.diplom.desktop.text.container.Attachment;
import com.serejs.diplom.desktop.utils.MarkDown;
import com.serejs.diplom.desktop.utils.Settings;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;

public class Fb2Loader extends AbstractLoader {
    /**
     * Функция получение глав книги из файла fb2
     *
     * @param uri Файл fb2
     */
    @Override
    public void load(URI uri) throws IOException {
        var file = new File(uri.getPath());
        var firstLine = FileUtils.readLines(file).get(0);

        var attrEncode = "encoding=\"";
        var startIndex = firstLine.indexOf(attrEncode) + attrEncode.length();

        var endEncode = "\"?>";
        var endIndex = firstLine.lastIndexOf(endEncode);

        var encoding = firstLine.substring(startIndex, endIndex);
        var doc = Jsoup.parse(file, encoding);


        //Получение только невложенных секций
        var allSections = doc.select("section section");
        var mainSections = doc.select("section").stream()
                .filter((Element sec) -> !allSections.contains(sec))
                .filter((Element sec) -> !sec.text().isEmpty())
                .toList();


        //Получение контента книги
        for (int i = 0; i < mainSections.size(); i++) {
            var section = mainSections.get(i);

            var secTitle = section.getElementsByTag("title").text();
            var title = file.getName() + " " + (i + 1) + " - " + Jsoup.clean(secTitle, Safelist.none());
            if (title.length() > Settings.getMaxLengthTitle()) {
                title = title.substring(0, Settings.getMaxLengthTitle());
            }

            //Собирание текста главы из всего текста в тегах
            var sb = new StringBuilder();
            section.getAllElements().forEach(el -> sb.append(Jsoup.clean(el.text(), Safelist.none())));

            fragments.put(title, sb.toString());


            //Сохранение приложений книги
            var fragmentAttachments = new HashSet<Attachment>();

            //Получение бинарников картинок
            var imageElements = section.getElementsByTag("img");
            imageElements.forEach(image -> {
                var href = image.attributes().get("l:href");
                if (href.isEmpty()) return;

                var imageEl = doc.getElementById(href.substring(1));
                if (imageEl == null) return;

                String name = href.substring(1);
                fragmentAttachments.add(
                        new Attachment(
                                name,
                                imageEl.text(),
                                AttachmentType.IMAGE)
                );
            });

            //Получение таблиц
            var tableElements = section.getElementsByTag("table");
            var tableSB = new StringBuilder();
            tableElements.forEach(table -> tableSB.append(MarkDown.mdTable(table)).append("\n\n"));
            if (!tableSB.isEmpty())
                fragmentAttachments.add(new Attachment(title, tableSB.toString(), AttachmentType.TABLE));
            attachments.put(title, fragmentAttachments);
        }
    }
}
