package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.text.container.View;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectClientController extends AbstractClientController {

    public static LinkedList<Project> getProjects(Long viewID) {
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
            projects.add(new Project(id, title));
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
