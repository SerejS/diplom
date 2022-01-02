package com.serejs.diplom.desktop.text.parse.web;

import com.serejs.diplom.desktop.text.container.Format;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.text.parse.file.LiteratureType;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GoogleSearch {
    private final StringBuilder baseUrl = new StringBuilder("https://customsearch.googleapis.com/customsearch/v1?");

    public GoogleSearch(String cx, String key) {
        baseUrl.append("cx=").append(cx).append('&');
        baseUrl.append("key=").append(key).append('&');
    }

    public HashMap<String, Format> getUrls (List<Theme> themes) {
        HashMap<String, Format> urls = new HashMap<>();

        for (Theme theme : themes) {
            for (String url : getUrls(theme.getTitle())) {
                urls.put(url, new Format(LiteratureType.WEB, null, null, null));
            }
        }

        return urls;
    }

    private HashSet<String> getUrls(String query) {
        HashSet<String> links = new HashSet<>();

        URL url;
        try {
            url = new URL(baseUrl + "q=" + query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return links;
        }

        String content;
        try {
            content = getResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
            return links;
        }

        JSONObject jo = new JSONObject(content);
        jo.getJSONArray("items").forEach((el) -> links.add(((JSONObject) el).get("link").toString()));

        return links;
    }

    private String getResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        for (String inputLine = in.readLine(); inputLine != null; inputLine = in.readLine()) {
            response.append(inputLine);
        }

        return response.toString();
    }
}
