package com.serejs.diplom.desktop.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.annotation.Experimental;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class GoogleSearchEngine implements JsonSerializable {
    private static final String baseGoogleSearch = "https://customsearch.googleapis.com/customsearch/v1?";

    private Long id;
    private String cx;
    private String token;
    private LiteratureType type;
    private Project project;

    public GoogleSearchEngine(String cx, String token, LiteratureType type, Project project)  {
        this.id = -1L;
        this.cx = cx;
        this.token = token;
        this.type = type;
        this.project = project;
    }

    /**
     * Поиск интернет-страниц по теме
     *
     * @param theme По какой теме происходит поиск
     * @return Список интернет-источников по теме
     */
    @Experimental
    public List<Source> getSources(Theme theme) throws IOException, URISyntaxException {
        var uriBuilder = new URIBuilder(baseGoogleSearch);

        var query = theme.getTitle();

        URL url = uriBuilder
                .addParameter("cx", cx)
                .addParameter("key", token)
                .addParameter("q", query)
                .build().toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        for (String inputLine = in.readLine(); inputLine != null; inputLine = in.readLine()) {
            response.append(inputLine);
        }
        connection.disconnect();

        String content = response.toString();

        //Элементы с ссылками на сайты
        var jArr = new JSONObject(content).getJSONArray("items");
        Set<URI> uris = new HashSet<>();
        for (int i = 0; i < jArr.toList().size(); i++) {
            uris.add(new URI(jArr.getJSONObject(i).get("link").toString()));
        }

        return uris.stream().map(uri -> new Source(-1L, uri, SourceType.WEB, type)).toList();
    }

    @Override
    public JsonObject toJson() {
        Gson gson = new Gson();
        var gseObj = gson.toJsonTree(this).getAsJsonObject();
        gseObj.remove("project");
        gseObj.remove("type");

        JsonObject nestedJson = null;
        if (project != null) {
            nestedJson = new JsonObject();
            nestedJson.addProperty("id", project.getId());
        }

        gseObj.add("project", nestedJson);

        nestedJson = null;
        if (project != null) {
            nestedJson = new JsonObject();
            nestedJson.addProperty("id", type.getId());
        }

        gseObj.add("type", nestedJson);

        return gseObj;
    }
}
