package com.serejs.diplom.desktop.ui.states;

import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.server.controllers.ProjectClientController;
import com.serejs.diplom.desktop.server.controllers.TypeClientController;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class State {
    @Getter
    @Setter
    private static User user;

    @Getter
    @Setter
    private static View view;
    private static LinkedList<Project> projects = new LinkedList<>();

    @Getter
    @Setter
    private static Project project;

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


    //Получение списка проектов
    public static LinkedList<Project> getProjects() throws IOException {
        if (projects.isEmpty())
            projects = ProjectClientController.getProjects(view);

        return projects;
    }


    //Создание нового проекта
    public static void createNewProject() {
        project = new Project(-1L, "");
        sources = new LinkedList<>();
        themes = new LinkedList<>();
    }

    //Установка полей существующего проекта
    public static void getProjectData(Long id) {
        project = projects.stream().filter(p -> Objects.equals(p.getId(), id)).findFirst().orElseThrow();
        themes = ProjectClientController.getThemes(id);
        sources = ProjectClientController.getSources(id);
        engines.addAll(ProjectClientController.getEngines(id));
    }


    public static LiteratureType getLitTypeById(Long id) {
        return getLitTypes().stream().filter(lt -> Objects.equals(lt.getId(), id)).findFirst().orElseThrow();
    }

    public static List<LiteratureType> getLitTypes() {
        if (types.isEmpty())
            try {
                types.addAll(TypeClientController.getTypes(view));
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

    public static void saveProject() {
        projects.add(new Project(project.getId(), project.getTitle()));
    }


    //Установка директории с файлами
    public static void setOutputDirectory(File dir) {
        if (dir.isDirectory())
            outputDirectory = dir;
    }
}
