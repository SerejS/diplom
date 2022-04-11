package com.serejs.diplom.desktop.ui.states;

import com.serejs.diplom.desktop.server.ServerClient;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import javafx.stage.DirectoryChooser;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class State {
    @Getter
    @Setter
    private static Long viewID;
    private static HashMap<Long, String> projects = new HashMap<>();

    @Getter
    @Setter
    private static String projectTitle;

    @Getter
    @Setter
    private static FragmentMap fragments;

    @Getter
    @Setter
    private static List<Source> sources;

    @Getter
    @Setter
    private static List<Theme> themes;

    private static final List<LiteratureType> types = new LinkedList<>();

    @Getter
    private static final List<GoogleSearchEngine> engines = new LinkedList<>();

    @Getter
    private static File outputDirectory;


    //Установка отображений
    public static void setView(Long id) {
        viewID = id;
    }

    //Получение списка проектов
    public static Map<Long, String> getProjects() throws IOException {
        if (projects.isEmpty())
            projects = ServerClient.getProjects(viewID);

        return projects;
    }


    //Создание нового проекта
    public static void createNewProject() {
        projectTitle = "";
        sources = new LinkedList<>();
        themes = new LinkedList<>();

        setOutputDirectory();
    }

    //Установка полей существующего проекта
    public static void openProject(Long id) {
        projectTitle = projects.get(id);
        sources = ServerClient.getSources(id);
        themes = ServerClient.getThemes(id);
        setOutputDirectory();
    }

    public static List<LiteratureType> getLitTypes() {
        if (types.isEmpty())
            try {
                types.addAll(ServerClient.getTypes(viewID));
            } catch (Exception e) {
                System.err.println("Ошибка получения типов литературы с сервера");
            }

        return types;
    }

    public static void removeLitType(LiteratureType type) {
        //Сделать получение из БД
        var defaultType = new LiteratureType("Общий", true);

        types.remove(type);
        sources.stream().filter(s -> s.getLitType() == type).forEach(s -> s.setLitType(defaultType));
    }


    public static void addSources(List<Source> newSources) {
        sources.addAll(newSources);
    }


    public static void addEngine(GoogleSearchEngine engine) {
        engines.add(engine);
    }


    //Установка директории с файлами
    public static void setOutputDirectory() {
        var chooser = new DirectoryChooser();
        try {
            var dir = chooser.showDialog(null);
            if (dir.isDirectory()) outputDirectory = dir;
        } catch (NullPointerException ignored) {
        }
    }
}
