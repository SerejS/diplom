package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.controllers.abstarts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ThemeController extends TableViewController<Theme> {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var title = new TableColumn<Theme, String>("Название темы");
        title.setMinWidth(200);
        title.setCellValueFactory(new PropertyValueFactory<>("title"));

        var percent = new TableColumn<Theme, Double>("Процентное содержание");
        percent.setMinWidth(200);
        percent.setCellValueFactory(new PropertyValueFactory<>("percent"));

        var keywords = new TableColumn<Theme, String>("Ключевые слова");
        keywords.setMinWidth(400);
        keywords.setCellValueFactory(new PropertyValueFactory<>("keyWords"));

        super.initialize(title, percent, keywords);
    }

    @FXML
    private void openModal() {
        openModal("modal-theme.fxml");
    }

    @FXML
    private void goNextPage() {
        App.setThemes(getItems());
        anotherPage(nextButton, "files-view.fxml");
    }


    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "project-overview.fxml");
    }
}
