package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import org.apache.http.HttpException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;

public class TestEngineController {
    @Before
    public void beforeEngineTests() {
        String rightUsername = System.getenv("rightUsername");
        String rightPassword = System.getenv("rightPassword");

        try {
            UserClientController.auth(rightUsername, rightPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка аутентификации c верными данными");
        }

        try {
            var view = ViewClientController.getViews().get(0);
            State.setView(view);
            var projects = ProjectClientController.getProjects(view);
            State.setProject(projects.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка получения отображения");
        }
    }

    @Test
    public void createEngines() {
        var view = State.getView();
        State.setView(view);

        LiteratureType lt;
        Project project = State.getProject();
        try {
            lt = TypeClientController.getTypes(view).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка получения типа литературы");
        }

        assert lt != null;

        var engineList = new LinkedList<GoogleSearchEngine>();
        var gse = new GoogleSearchEngine("1", "2", lt, project);
        engineList.add(gse);

        try {
            EngineClientController.sendEngines(engineList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка отправки параметров поисковых систем");
        }
    }


    @Test
    public void getProjectEngines() {
        var project = State.getProject();
        try {
            var engines = EngineClientController.getEngines(project);
            State.getEngines().addAll(engines);
        } catch (HttpException | IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка получения параметров поисковых систем");
        }
    }


    @Test
    @After
    public void deleteProjectEngines() {
        var engines = State.getEngines();

        for (var engine : engines) {
            try {
                EngineClientController.deleteEngine(engine);
            } catch (HttpException | IOException | URISyntaxException e) {
                e.printStackTrace();
                throw new AssertionError("Ошибка удаления параметров поисковых систем");
            }
        }
    }
}
