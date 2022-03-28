package com.serejs.diplom.desktop.utils;

import com.serejs.diplom.desktop.enums.AttachmentType;
import com.serejs.diplom.desktop.text.container.Attachment;
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

    private static Set<Attachment> imgFromXML(Document doc) {
        var imageAttachments = new HashSet<Attachment>();
        doc.getElementsByTag("img").forEach(((Element img) ->
                img.attributes().forEach(attr -> {
                    if (attr.getKey().equals("src")) {
                        var name = attr.getValue();
                        if (name.contains("/")) name = name.substring(name.lastIndexOf("/") + 1);

                        imageAttachments.add(new Attachment(name, attr.getValue(), AttachmentType.IMAGE));
                    }
                })
        ));
        return imageAttachments;
    }

    private static Set<Attachment> tablesFromXML(Document doc) {
        var tablesAttachments = new HashSet<Attachment>();
        doc.getElementsByTag("table").forEach((Element table) ->
                tablesAttachments.add(new Attachment("", MarkDown.mdTable(table), AttachmentType.TABLE)));
        return tablesAttachments;
    }

    private static Set<Attachment> audioFromXML(Document doc) {
        var audioAttachments = new HashSet<Attachment>();
        doc.getElementsByTag("audio").forEach((Element audio) ->
                audio.getElementsByTag("source").forEach((Element source) ->
                        source.attributes().forEach(attr -> {
                            if (attr.getKey().equals("src")) {
                                var name = attr.getValue();
                                if (name.contains("/")) name = name.substring(name.lastIndexOf("/") + 1);

                                audioAttachments.add(new Attachment(name, attr.getValue(), AttachmentType.AUDIO));
                            }
                        })
                )
        );
        return audioAttachments;
    }
}
