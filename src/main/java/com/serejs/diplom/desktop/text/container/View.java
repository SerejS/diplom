package com.serejs.diplom.desktop.text.container;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class View implements JsonSerializable {
    private Long id;
    private final String title;

    @Override
    public String toString() {
        return title;
    }

    @Override
    public JsonObject toJson() {
        var gson = new Gson();
        return gson.toJsonTree(this).getAsJsonObject();
    }
}
