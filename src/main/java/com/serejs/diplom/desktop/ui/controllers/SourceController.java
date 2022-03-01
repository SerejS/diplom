package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URISyntaxException;

public class SourceController extends RootController {
    @FXML
    private TextField mainCX;
    @FXML
    private TextField mainKey;
    @FXML
    private Button prevButton;
    @FXML
    private Button analiseButton;

    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "files-view.fxml");
    }

    @FXML
    protected void goResultPage() throws URISyntaxException, IOException {
        String cx = mainCX.getText();
        String key = mainKey.getText();

        if (!cx.isEmpty() && !key.isEmpty()) {
            App.addSources(new GoogleSearchEngine(cx, key));
        }

        anotherPage(analiseButton, "result-view.fxml");
    }
}
