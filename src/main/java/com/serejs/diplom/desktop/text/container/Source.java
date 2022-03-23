package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.enums.SourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Getter
@AllArgsConstructor
public class Source {
    private URI uri;
    private SourceType sourceType;
    @Setter
    private LiteratureType litType;
}
