package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.states.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

public class TestClientProjectController {
    @Before
    public void login() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProjects() {
        var view = State.getView();
        assert view != null;

        try {
            ProjectClientController.getProjects(view);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка получения проектов с верными данными");
        }
    }


    @Test
    public void testCreateProject() {
        var view = State.getView();
        assert view != null;

        Long id = -1L;
        try {
            var project = new Project(id, "testName", view, Date.valueOf(LocalDate.now()));

            ProjectClientController.createProject(project);

            id = project.getId();
            Assert.assertNotEquals(-1L, id.longValue());

            State.setProject(project);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка создания проекта");
        }

        try {
            var ids = ProjectClientController.getProjects(view).stream()
                    .mapToLong(Project::getId).boxed().toList();

            if (!ids.contains(id)) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка записи проекта");
        }


    }


    @Test
    public void testDeleteProject() {
        try {
            var project = State.getProject();
            State.clearProject();

            ProjectClientController.deleteProject(project);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка удаления проекта");
        }
    }
}
