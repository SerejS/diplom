package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.enums.SourceType;

import java.net.URI;

public class Source {
    private URI uri;
    private SourceType type;

    public Source(URI uri, SourceType type) {
        this.uri = uri;
        this.type = type;
    }


    public URI getUri() {
        return uri;
    }

    public SourceType getType() {
        return type;
    }
}
