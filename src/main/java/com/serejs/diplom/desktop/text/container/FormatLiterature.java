package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.enums.SourceType;
import lombok.Getter;

import java.net.URI;

public class FormatLiterature extends Literature {
    @Getter
    private final Format format;

    public FormatLiterature(Long id, URI uri, SourceType sourceType, LiteratureType litType, Format format) {
        super(id, uri, sourceType, litType);
        this.format = format;
    }


}
