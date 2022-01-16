package com.serejs.diplom.desktop.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ThemeController extends RootController {
    @FXML private Button prevButton;
    @FXML private Button addButton;
    @FXML private Button nextButton;
    @FXML private TableView tableTheme;


    @FXML
    private void addTheme() {
        openModal(addButton, this,"modal-theme.fxml");
    }

    public void printTheme(String theme) {
        tableTheme.getItems().add(0, theme);
        System.out.println("Попытка добавить элемент " + theme);
    }

    @FXML
    private void goNextPage() {
        anotherPage(nextButton, "files-view.fxml");
    }


    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "project-overview.fxml");
    }
}
