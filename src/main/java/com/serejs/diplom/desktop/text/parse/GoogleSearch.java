package com.serejs.diplom.desktop.text.parse;

import com.serejs.diplom.desktop.text.container.Theme;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class GoogleSearch {
    private final static StringBuilder baseUrl = new StringBuilder("https://customsearch.googleapis.com/customsearch/v1?");

    public GoogleSearch(String cx, String key) {
        baseUrl.append("cx=").append(cx).append('&');
        baseUrl.append("key=").append(key).append('&');
    }

    public HashMap<String, Set<String>> getUrls(List<Theme> themes) {
        HashMap<String, Set<String>> allUrls = new HashMap<>();

        for (Theme theme : themes) {
            HashMap<String, Set<String>> themeUrls = getUrls(theme.getTitle());

            themeUrls.keySet().forEach(site -> {
                Set<String> urls = allUrls.get(site);
                if (urls != null) {
                    urls.addAll(themeUrls.get(site));
                } else {
                    urls = themeUrls.get(site);
                }

                allUrls.put(site, urls);
            });
        }

        return allUrls;
    }

    private HashMap<String, Set<String>> getUrls(String query) {
        HashMap<String, Set<String>> links = new HashMap<>();

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

        //Массив найденных элементов
        JSONArray jArr = new JSONObject(content).getJSONArray("items");
        //Получение сайтов, по которым производится поиск
        Set<String> sites = new HashSet<>();
        for (int i = 0; i < jArr.toList().size(); i++) {
            sites.add(jArr.getJSONObject(i).get("displayLink").toString());
        }

        //Распределение найденных ссылок по сайтам, где они найдены
        sites.forEach(site -> {
            Set<String> siteLinks = new HashSet<>();
            for (int i = 0; i < jArr.toList().size(); i++) {
                if (jArr.getJSONObject(i).getString("displayLink").equals(site)) {
                    siteLinks.add(jArr.getJSONObject(i).getString("link"));
                }
            }

            links.put(site, siteLinks);
        });


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
