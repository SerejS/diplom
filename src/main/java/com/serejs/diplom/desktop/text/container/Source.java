package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.enums.SourceType;

import java.net.URI;

public class Source {
    private URI uri;
    private SourceType sourceType;
    private LiteratureType litType;

    public Source(URI uri, SourceType sourceType, LiteratureType litType) {
        this.uri = uri;
        this.sourceType = sourceType;
        this.litType = litType;
    }


    public URI getUri() {
        return uri;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public LiteratureType getLitType() {
        return litType;
    }

    public void setLitType(LiteratureType type) {
        this.litType = type;
    }
}
