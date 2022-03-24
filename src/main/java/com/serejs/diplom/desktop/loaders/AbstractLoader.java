package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Attachment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractLoader implements ContentLoader {
    protected Map<String, String> fragments = new HashMap<>();
    protected Map<String, Set<Attachment>> attachments = new HashMap<>();

    @Override
    public Set<Attachment> getAttachments(String key) {
        return attachments.getOrDefault(key, new HashSet<>());
    }

    @Override
    public Map<String, String> getContent() {
        return fragments;
    }
}
