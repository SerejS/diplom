package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.text.container.View;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectClientController extends AbstractClientController {
    public static void createProject(Project project) {
        try {
            var resp = postRequest("/api/project", project);
            var id = Long.parseLong(resp);
            project.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Project> getProjects(View view) {
        var viewID = view.getId();
        var projects = new LinkedList<Project>();

        JSONArray jsonProjects;
        try {
            var params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("viewId", String.valueOf(viewID)));

            jsonProjects = new JSONArray(getRequest("/api/projects", params));
        } catch (Exception e) {
            return projects;
        }

        for (int i = 0; i < jsonProjects.length(); i++) {
            var jsonProject = jsonProjects.getJSONObject(i);

            var id = jsonProject.getBigInteger("id").longValue();
            var title = jsonProject.getString("title");
            projects.add(new Project(id, title, view));
        }

        return projects;
    }

    public static List<Theme> getThemes(long projectID) {
        //ИД - тема
        Map<Long, Theme> themes = new HashMap<>();
        //ИД темы -  ИД родительской темы
        Map<Long, Long> rootThemes = new HashMap<>();

        //Получение тем проекта с сервера
        JSONArray jsonThemes;
        try {
            var params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("projectId", String.valueOf(projectID)));

            jsonThemes = new JSONArray(getRequest("/api/themes", params));
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
                    projectID,
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

    public static void sendThemes(List<Theme> themes) {
        try {
            postRequest("/api/themes", themes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addSources(List<Source> sources) {

    }

    public static List<Source> getSources(long projectId) {
        return new LinkedList<>();
    }

    public static List<GoogleSearchEngine> getEngines(Project project) {
        LinkedList<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("projectId", String.valueOf(project.getId())));

        var engines = new LinkedList<GoogleSearchEngine>();
        try {
            var response = getRequest("/api/engines", params);

            var list = new JSONArray(response);
            for (int i = 0; i < list.length(); i++) {
                var jsonObj = list.getJSONObject(i);

                var gse = new GoogleSearchEngine(
                        jsonObj.getLong("id"),
                        jsonObj.getString("cx"),
                        jsonObj.getString("token"),
                        State.getLitTypeById(jsonObj.getJSONObject("type").getLong("id")),
                        project
                );

                engines.add(gse);
            }
        } catch (HttpException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return engines;
    }

    //Отправление оторажений на сервер
    public static void sendEngines(List<GoogleSearchEngine> engines) {
        try {
            postRequest("/api/engines", engines);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
