package com.serejs.diplom.desktop.ui.states;

import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.server.controllers.*;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
    private static FragmentMap fragments = new FragmentMap();

    @Getter
    @Setter
    private static List<Literature> literatures = new LinkedList<>();

    @Getter
    @Setter
    private static List<Theme> themes = new LinkedList<>();

    private static final List<LiteratureType> types = new LinkedList<>();

    @Getter
    private static List<GoogleSearchEngine> engines = new LinkedList<>();

    @Getter
    private static File outputDirectory;

    @Getter
    @Setter
    private static File rpdFile;


    //Получение списка проектов
    public static LinkedList<Project> getProjects() throws IOException, HttpException, URISyntaxException {
        if (projects.isEmpty())
            projects = ProjectClientController.getProjects(view);

        return projects;
    }


    //Установка полей существующего проекта
    public static void getProjectData(Project selectedProject) throws HttpException, IOException, URISyntaxException {
        project = selectedProject;
        themes = ThemeClientController.getThemes(selectedProject);
        literatures = LiteratureClientController.getLiteratures(selectedProject);
        engines = EngineClientController.getEngines(selectedProject);
    }

    public static void clearProject() {
        project = null;

        fragments.clear();
        literatures.clear();
        themes = new LinkedList<>();
        engines.clear();
        outputDirectory = null;
    }



    public static LiteratureType getLitTypeById(Long id) {
        return getLitTypes().stream().filter(lt -> Objects.equals(lt.getId(), id)).findFirst().orElseThrow();
    }

    public static List<LiteratureType> getLitTypes() {
        if (!types.isEmpty()) return types;

        try {
            types.addAll(TypeClientController.getTypes(view));
        } catch (Exception e) {
            System.err.println("Ошибка получения типов литературы с сервера");
        }

        return types;
    }

    public static void saveProjectData() throws Exception {
        ThemeClientController.sendThemes(themes);

        for (var literature : literatures) {
            FileClientController.upload(literature);
        }

        EngineClientController.sendEngines(engines);
    }


    //Установка директории с файлами
    public static void setOutputDirectory(File dir) {
        if (dir.isDirectory())
            outputDirectory = dir;
    }
}
