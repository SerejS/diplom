package com.serejs.diplom.desktop.text.container;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Format implements JsonSerializable {
    @Setter
    private Long id;
    private String prev;
    private String mid;
    private String after;

    @Override
    public JsonObject toJson() {
        var formatJson = new JsonObject();

        formatJson.addProperty("after", after);
        formatJson.addProperty("prev", prev);
        formatJson.addProperty("mid", mid);

        return formatJson;
    }
}
