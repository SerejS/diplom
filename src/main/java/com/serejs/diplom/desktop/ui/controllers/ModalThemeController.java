package com.serejs.diplom.desktop.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ModalThemeController implements Initializable {
    @FXML
    private TextField titleTheme;
    @FXML
    private TextArea textArea;
    @FXML
    private Slider slider;
    @FXML
    private Button addButton;

    private RootController parent;

    public void setParent(RootController parentController) {
        this.parent = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (textArea != null) textArea.setWrapText(true);
    }

    public void printValue() {
        if (parent == null) return;
        System.out.println("Hey");

    }
}
