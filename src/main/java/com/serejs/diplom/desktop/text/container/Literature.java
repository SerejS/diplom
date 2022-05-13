package com.serejs.diplom.desktop.text.container;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.serejs.diplom.desktop.enums.SourceType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;

@Data
@AllArgsConstructor
public class Literature implements JsonSerializable {
    private Long id;
    private URI uri;
    private SourceType sourceType;
    private LiteratureType litType;

    @Override
    public JsonObject toJson() {
        var gson = new Gson();
        var element = gson.toJsonTree(this);
        return element.getAsJsonObject();
    }
}
