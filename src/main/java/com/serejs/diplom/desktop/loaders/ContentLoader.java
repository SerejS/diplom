package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Attachment;
import com.serejs.diplom.desktop.text.container.Source;

import java.util.Map;
import java.util.Set;

public interface ContentLoader {
    void load(Source source) throws Exception;

    Map<String, String> getContent();

    Set<Attachment> getAttachments(String key);
}
