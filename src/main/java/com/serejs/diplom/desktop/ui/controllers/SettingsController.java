package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends RootController {
    @FXML
    Button nextButton;
    @FXML
    Button prevButton;

    @FXML
    private void goNextPage() {
        anotherPage(nextButton, "result-view.fxml");
    }


    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "sources-view.fxml");
    }
}
