package com.serejs.diplom.desktop.server;


import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class ServerClient {
    private static final String baseUrl = "http://localhost:8080";
    private static User user = null;

    private static String getJson(String endpoint) throws IOException, HttpException {
        var name = user.getUsername();
        var pass = user.getPassword();

        HttpClient httpclient = HttpClients.createDefault();
        var httppost = new HttpGet(baseUrl + endpoint);

        // Заголовок запроса
        String encoding = Base64.getEncoder().encodeToString((name + ":" + pass).getBytes(StandardCharsets.UTF_8));
        httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);

        HttpResponse response = httpclient.execute(httppost);
        var responseCode = response.getStatusLine().getStatusCode();
        if (responseCode != 200) throw new HttpException("Ошибка авторизации. " + responseCode);

        try (var is = response.getEntity().getContent()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder jsonText = new StringBuilder();

            int cp;
            while ((cp = rd.read()) != -1)
                jsonText.append((char) cp);

            return jsonText.toString();
        }
    }


    public static Map<Long, String> getViews() throws IOException, HttpException {
        var jsonViews = new JSONArray(getJson("/api/views"));

        var views = new HashMap<Long, String>();
        for (int i = 0; i < jsonViews.length(); i++) {
            var jsonView = jsonViews.getJSONObject(i);

            views.put(jsonView.getBigInteger("id").longValue(), jsonView.getString("title"));
        }

        return views;
    }


    public static void register(String username, String pass) throws IOException, HttpException {
        User registrationUser;
        try {
            registrationUser = new User(username, pass);
        } catch (Exception e) {
            ErrorAlert.info("Ошибка генерации пароля");
            return;
        }

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(baseUrl + "/user/registration");

        // Параметры запроса
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", registrationUser.getPassword()));
        httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        HttpResponse response = httpclient.execute(httppost);

        if (response.getStatusLine().getStatusCode() != 200) throw new HttpException("Ошибка регистрации");
    }


    public static void auth(String username, String password) throws Exception {
        user = new User(username, password);
        getViews();
    }


    public static void addView(String viewName) {
        //Отправление на сервер/добавление
    }

    public static LinkedList<Project> getProjects(Long viewID) {
        var projects = new LinkedList<Project>();

        JSONArray jsonProjects;
        try {
            jsonProjects = new JSONArray(getJson("api/projects?viewId=" + viewID));
        } catch (Exception e) {
            return projects;
        }

        for (int i = 0; i < jsonProjects.length(); i++) {
            var jsonProject = jsonProjects.getJSONObject(i);

            var id = jsonProject.getBigInteger("id").longValue();
            var title = jsonProject.getString("title");
            projects.add(new Project(id, title));
        }

        return projects;
    }


    public static List<LiteratureType> getTypes(long viewID) throws IOException, HttpException {
        List<LiteratureType> types = new LinkedList<>();

        var jsonLitType = new JSONArray(getJson("/types?viewId=" + viewID));
        for (int i = 0; i < jsonLitType.length(); i++) {
            var jsonLitTypes = jsonLitType.getJSONObject(i);

            var type = new LiteratureType(
                    jsonLitTypes.getLong("id"),
                    jsonLitTypes.getString("title"),
                    jsonLitTypes.getBoolean("main"));
            types.add(type);
        }

        return types;
    }

    public static List<Theme> getThemes(long projectID) {
        //ИД - тема
        Map<Long, Theme> themes = new HashMap<>();
        //ИД темы -  ИД родительской темы
        Map<Long, Long> rootThemes = new HashMap<>();

        //Получение тем проекта с сервера
        JSONArray jsonThemes;
        try {
            jsonThemes = new JSONArray(getJson("/themes?projectId=" + projectID));
        } catch (Exception e) {
            return new LinkedList<>();
        }

        for (int i = 0; i < jsonThemes.length(); i++) {
            var jsonTheme = jsonThemes.getJSONObject(i);

            var types = jsonTheme.getJSONArray("types").toList().stream()
                    .map(obj -> State.getLitTypeById(Long.parseLong(obj.toString())))
                    .collect(Collectors.toSet());


            var themeID = jsonTheme.getLong("id");
            var theme = new Theme(
                    themeID,
                    null,
                    jsonTheme.getString("title"),
                    jsonTheme.getDouble("percent"),
                    jsonTheme.getString("ngrams"),
                    types
            );


            //Добавление найденной темы и ИД её родителя в мапы
            if (!jsonTheme.isNull("root")) rootThemes.put(themeID, jsonTheme.getLong("root"));
            themes.put(themeID, theme);
        }

        //Установка родительских тем
        themes.forEach((id, theme) -> {
            Long rootThemeID = rootThemes.get(id);
            Theme rootTheme = themes.get(rootThemeID);
            theme.setRoot(rootTheme);
        });

        return themes.values().stream().toList();
    }


    public static String getProjectTitle(long projectId) {
        return "Название давно созданного проекта!";
    }

    public static List<Source> getSources(long projectId) {
        return new LinkedList<>();
    }

    public static List<Source> getEngines(long projectId) {
        return new LinkedList<>();
    }


}
