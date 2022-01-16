package com.serejs.diplom.desktop.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class SourceController extends RootController {
    @FXML private Button prevButton;
    @FXML private Button analiseButton;

    @FXML
    FileChooser fileChooser = new FileChooser();

    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "files-view.fxml");
    }

    @FXML
    protected void goResultPage() {anotherPage(analiseButton, "result-view.fxml");}
}
