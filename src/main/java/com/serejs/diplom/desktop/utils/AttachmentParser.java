package com.serejs.diplom.desktop.utils;

import com.serejs.diplom.desktop.enums.AttachmentType;
import com.serejs.diplom.desktop.text.container.Attachment;
import javafx.util.Pair;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;

public class AttachmentParser {

    public static Set<Attachment> xmlAttachments(Element document) {
        var xmlAttachments = new HashSet<Attachment>();
        if (document instanceof Document doc) {
            xmlAttachments.addAll(imgFromXML(doc));
            xmlAttachments.addAll(audioFromXML(doc));
            xmlAttachments.addAll(tablesFromXML(doc));
        }
        return xmlAttachments;
    }

    private static HashSet<Attachment> imgFromXML(Document doc) {
        var imageAttachments = new HashSet<Attachment>();
        doc.getElementsByTag("img").forEach(((Element img) -> {
            Pair<String, String> pair = tagSource(img);
            if (pair == null) return;

            imageAttachments.add(new Attachment(pair.getKey(), pair.getValue(), AttachmentType.IMAGE));
        }
        ));
        return imageAttachments;
    }

    private static Set<Attachment> tablesFromXML(Document doc) {
        var tablesAttachments = new HashSet<Attachment>();
        doc.getElementsByTag("table").forEach((Element table) ->
                tablesAttachments.add(new Attachment("", MarkDown.mdTable(table), AttachmentType.TABLE)));
        return tablesAttachments;
    }

    private static HashSet<Attachment> audioFromXML(Document doc) {
        var audioAttachments = new HashSet<Attachment>();
        doc.getElementsByTag("audio").forEach((Element audio) ->
            audio.getElementsByTag("source").forEach((Element source) -> {
                Pair<String, String> pair = tagSource(source);
                if (pair == null) return;

                audioAttachments.add(new Attachment(pair.getKey(), pair.getValue(), AttachmentType.AUDIO));
            })
        );
        return audioAttachments;
    }


    /**
     * Возвращает файл и ссылку на него
     * @param source Тег в котором лежит аттрибут с сылкой
     * @return Пара: название, ссылка на файл
     */
    private static Pair<String, String> tagSource(Element source) {
        for (Attribute attr : source.attributes()) {
            if (attr.getKey().equals("src")) {
                var name = attr.getValue();
                if (name.contains("/")) name = name.substring(name.lastIndexOf("/") + 1);

                return new Pair<>(name, attr.getValue());
            }
        }

        return null;
    }
}
