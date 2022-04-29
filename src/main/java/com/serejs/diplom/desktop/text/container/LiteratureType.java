package com.serejs.diplom.desktop.text.container;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LiteratureType implements JsonSerializable {
    private Long id;
    private final String title;
    private final boolean main;
    private final View view;

    public LiteratureType(String title, boolean main, View view) {
        this.id = -1L;
        this.title = title;
        this.main = main;
        this.view = view;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public JsonObject toJson() {
        Gson gson = new Gson();
        var typeObj = gson.toJsonTree(this).getAsJsonObject();
        typeObj.remove("view");

        JsonObject nestedJson = null;
        if (view != null) {
            nestedJson = new JsonObject();
            nestedJson.addProperty("id", view.getId());
        }

        typeObj.add("view", nestedJson);

        return typeObj;
    }
}
