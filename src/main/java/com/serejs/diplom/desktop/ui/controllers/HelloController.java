package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import javafx.fxml.FXML;

public class HelloController extends RootController {
    @FXML
    private javafx.scene.control.Button login;

    @FXML
    protected void handleLoginEvent() {
        anotherPage(login, "user-view.fxml");
    }

    @FXML
    protected void registration() {
        System.out.println("Reg click");
    }
}