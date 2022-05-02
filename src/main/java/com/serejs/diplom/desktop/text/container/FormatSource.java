package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.enums.SourceType;
import lombok.Getter;

import java.net.URI;

public class FormatSource extends Source {
    @Getter
    private final Format format;

    public FormatSource(Long id, URI uri, SourceType sourceType, LiteratureType litType, Format format) {
        super(id, uri, sourceType, litType);
        this.format = format;
    }


}
