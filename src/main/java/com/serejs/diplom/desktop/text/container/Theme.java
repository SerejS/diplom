package com.serejs.diplom.desktop.text.container;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.serejs.diplom.desktop.analyze.Analyzer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;


@Data
@EqualsAndHashCode
//Percent относительно root -> фактический
public class Theme implements JsonSerializable {
    private Long id;
    private Theme root;
    private String title;
    private double percent;
    private String textKeyNGrams;
    private Set<String> keyNGrams;
    private Set<LiteratureType> types;

    private Project project;

    public Theme(Theme root, Project project, String title, double percent, String textKeyNGrams, Set<LiteratureType> types) {
        this.root = root;
        this.title = title;
        this.textKeyNGrams = textKeyNGrams;
        this.keyNGrams = new HashSet<>(Analyzer.parseNGrams(textKeyNGrams));
        this.types = types;
        this.project = project;

        if (this.root != null) {
            this.types.addAll(root.types);
            this.keyNGrams.addAll(root.keyNGrams);
            this.percent = percent * root.percent / 100.;
        } else {
            this.percent = percent / 100;
        }
    }


    public Theme(Long id, Project project, Theme root, String title, double percent, String textKeyNGrams, Set<LiteratureType> types) {
        this(root, project, title, percent, textKeyNGrams, types);
        this.id = id;
    }

    public void setKeyNGrams(String textKeyNGrams) {
        this.textKeyNGrams = textKeyNGrams;
        this.keyNGrams = new HashSet<>(Analyzer.parseNGrams(textKeyNGrams));
        if (root != null) this.keyNGrams.addAll(root.keyNGrams);
    }

    @Override
    public JsonObject toJson() {
        Gson gson = new Gson();
        var themeObj = gson.toJsonTree(this).getAsJsonObject();
        themeObj.remove("project");

        JsonObject nestedJson = null;
        if (project != null) {
            nestedJson = new JsonObject();
            nestedJson.addProperty("id", project.getId());
        }
        themeObj.add("project", nestedJson);

        //Замена объектов типов литературы на их идентификаторы
        JsonArray nestedJsonArray = null;
        if (types != null) {
            nestedJsonArray = new JsonArray();

            for (var type : types) {
                var typeObject = new JsonObject();
                typeObject.addProperty("id", type.getId());
                nestedJsonArray.add(typeObject);
            }
        }
        themeObj.add("types", nestedJsonArray);

        return themeObj;
    }

    @Override
    public String toString() {
        return title;
    }
}
