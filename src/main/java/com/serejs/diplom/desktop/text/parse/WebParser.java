package com.serejs.diplom.desktop.text.parse;

import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.Theme;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public class WebParser {
    private final URIBuilder uriBuilder = new URIBuilder("https://customsearch.googleapis.com/customsearch/v1?");

    public WebParser(String cx, String key) throws URISyntaxException {
        uriBuilder.addParameter("cx", cx);
        uriBuilder.addParameter("key", key);
    }


    public List<Literature> literatureFromWeb(List<Theme> themes) {
        HashMap<String, Set<String>> siteURLs = getSiteURLs(themes);

        List<Literature> literatures = new LinkedList<>();

        for (String title : siteURLs.keySet()) {
            Set<String> links = siteURLs.get(title);
            HashMap<String, String> fragments = new HashMap<>();

            for (String link : links) {
                StringBuilder sb = new StringBuilder();
                Document doc;

                try {
                    doc = Jsoup.connect(link).get();
                } catch (IOException e) {
                    System.err.println("Ошибка получения текста из " + link);
                    continue;
                }

                doc.getElementsByTag("p").forEach((element) -> sb.append(element.text()).append('\n'));

                fragments.put(link, sb.toString());
            }

            literatures.add(new Literature(title, fragments, true));
        }

        return literatures;
    }

    private HashMap<String, Set<String>> getSiteURLs(List<Theme> themes) {
        HashMap<String, Set<String>> allUrls = new HashMap<>();

        for (Theme theme : themes) {
            HashMap<String, Set<String>> themeUrls = getSites(theme.title());

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

    private HashMap<String, Set<String>> getSites(String query) {
        HashMap<String, Set<String>> sites = new HashMap<>();

        URL url;
        try {
            url = uriBuilder.addParameter("q", query).build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            return sites;
        }

        String content;
        try {
            content = getEngineResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
            return sites;
        }

        //Массив найденных элементов
        JSONArray jArr = new JSONObject(content).getJSONArray("items");
        //Получение сайтов, по которым производится поиск
        Set<String> siteNames = new HashSet<>();
        for (int i = 0; i < jArr.toList().size(); i++) {
            siteNames.add(jArr.getJSONObject(i).get("displayLink").toString());
        }

        //Распределение найденных ссылок по сайтам, где они найдены
        siteNames.forEach(site -> {
            Set<String> siteLinks = new HashSet<>();
            for (int i = 0; i < jArr.toList().size(); i++) {
                if (jArr.getJSONObject(i).getString("displayLink").equals(site)) {
                    siteLinks.add(jArr.getJSONObject(i).getString("link"));
                }
            }

            sites.put(site, siteLinks);
        });


        return sites;
    }

    private String getEngineResponse(URL url) throws IOException {
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
