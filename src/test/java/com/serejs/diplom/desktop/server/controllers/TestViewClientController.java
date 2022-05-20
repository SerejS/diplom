package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.View;
import com.serejs.diplom.desktop.ui.states.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestViewClientController {
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
    }

    @Test
    public void testCreateView() {
        Long id;
        try {
            var name = "testName";

            var view = ViewClientController.sendView(name);
            Assert.assertNotEquals(-1L, view.getId().longValue());

            id = view.getId();
            State.setView(view);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка создания проекта");
        }

        try {
            var ids = ViewClientController.getViews().stream()
                    .mapToLong(View::getId).boxed().toList();

            if (!ids.contains(id)) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка записи отображения");
        }
    }

    @Test
    public void testGetViews() {
        try {
            ViewClientController.getViews();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка получения отображений с верными данными");
        }
    }

    @Test
    public void testDeleteView() {
        try {
            var view = State.getView();
            State.setView(null);

            ViewClientController.deleteView(view);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка удаления проекта");
        }
    }
}

