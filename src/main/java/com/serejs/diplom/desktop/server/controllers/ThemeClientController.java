package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.states.State;
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

public class ThemeClientController extends AbstractClientController{
    public static List<Theme> getThemes(Project project) {
        //ИД - тема
        Map<Long, Theme> themes = new HashMap<>();
        //ИД темы -  ИД родительской темы
        Map<Long, Long> rootThemes = new HashMap<>();

        //Получение тем проекта с сервера
        JSONArray jsonThemes;
        try {
            var params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("projectId", String.valueOf(project.getId())));

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
                    project,
                    null,
                    jsonTheme.getString("title"),
                    jsonTheme.getDouble("percent"),
                    jsonTheme.getString("textKeyNGrams"),
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

    public static void sendThemes(List<Theme> themes) throws HttpException, IOException, URISyntaxException {
        postRequest("/api/themes", themes);
    }
}
