package com.serejs.diplom.desktop.text.container;

import com.google.gson.JsonObject;
import com.serejs.diplom.desktop.enums.SourceType;
import lombok.Getter;

import java.net.URI;

public class FormatLiterature extends Literature {
    @Getter
    private final Format format;

    public FormatLiterature(Long id, URI uri, SourceType sourceType, LiteratureType litType, Project project, Format format) {
        super(id, uri, sourceType, litType, project);
        this.format = format;
    }

    public JsonObject toJson(Long formatId) {
        var litObj = super.toJson();
        if (format == null || formatId == -1L) return litObj;

        var nestedObject = new JsonObject();
        nestedObject.addProperty("id", formatId);

        litObj.add("format", nestedObject);

        return litObj;
    }
}
