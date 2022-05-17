package com.serejs.diplom.desktop.text.container;

import com.google.gson.JsonObject;
import com.serejs.diplom.desktop.enums.SourceType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.net.URI;

@Data
@AllArgsConstructor
public class Literature implements JsonSerializable {
    private Long id;
    private URI uri;
    private SourceType sourceType;
    private LiteratureType litType;

    private Project project;

    @Override
    public JsonObject toJson() {
        var litObj = new JsonObject();

        litObj.addProperty("source", sourceType.ordinal());
        litObj.addProperty("path", new File(uri).getName());

        JsonObject nestedJson = null;
        if (litType != null) {
            nestedJson = new JsonObject();
            nestedJson.addProperty("id", litType.getId());
        }
        litObj.add("type", nestedJson);

        nestedJson = null;
        if (project != null) {
            nestedJson = new JsonObject();
            nestedJson.addProperty("id", project.getId());
        }
        litObj.add("project", nestedJson);

        return litObj;
    }
}
