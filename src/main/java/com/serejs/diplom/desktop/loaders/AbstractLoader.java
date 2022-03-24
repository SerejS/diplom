package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Attachment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractLoader implements ContentLoader {
    protected Map<String, String> fragments;
    protected Map<String, Set<Attachment>> attachments;

    @Override
    public Set<Attachment> getAttachments(String key) {
        return attachments.getOrDefault(key, new HashSet<>());
    }

    @Override
    public Map<String, String> getContent() {
        return fragments;
    }
}
