package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Attachment;

import java.net.URI;
import java.util.Map;
import java.util.Set;

public interface ContentLoader {
    void load(URI uri) throws Exception;

    Map<String, String> getContent();

    Set<Attachment> getAttachments(String key);
}
