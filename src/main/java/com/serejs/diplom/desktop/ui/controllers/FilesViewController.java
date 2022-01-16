package com.serejs.diplom.desktop.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FilesViewController extends RootController {
    @FXML
    private Button nextButton;
    @FXML
    private Button prevButton;

    @FXML
    public void addFile() {

    }

    @FXML
    public void onNextPage() {
        anotherPage(nextButton, "sources-view.fxml");
    }

    @FXML
    public void onPrevPage() {anotherPage(prevButton, "theme-view.fxml");}
}
