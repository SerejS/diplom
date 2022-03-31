package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ThemeController extends TableViewController<Theme> {
    @FXML
    TextField titleProject;
    String modalFileName = "modal-theme.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var project = App.getProjectTitle();
        if (project != null) titleProject.setText(project);

        var themes = App.getThemes();
        if (themes != null) table.getItems().addAll(themes);

        var title = new TableColumn<Theme, String>("Название темы");
        title.setMinWidth(200);
        title.setCellValueFactory(new PropertyValueFactory<>("title"));

        var percent = new TableColumn<Theme, Double>("Процентное содержание");
        percent.setMinWidth(200);
        percent.setCellValueFactory(new PropertyValueFactory<>("percent"));

       /* var keywords = new TableColumn<Theme, String>("Ключевые слова");
        keywords.setMinWidth(400);
        keywords.setCellValueFactory(new PropertyValueFactory<>("keyWords"));*/

        var root = new TableColumn<Theme, String>("Родительская тема");
        root.setMinWidth(200);
        root.setCellValueFactory(new PropertyValueFactory<>("root"));

        super.initialize(modalFileName, title, percent, root);
    }

    @FXML
    private void openModal() {
        var percent = table.getItems().stream().filter(el -> el.getRoot() == null).mapToDouble(Theme::getPercent).sum();
        if (percent >= 1) {
            ErrorAlert.info("Сумма процентов основных тем достигла предела.");
            return;
        }

        openModal(modalFileName);
    }

    @FXML
    private void goNextPage() {
        var title = titleProject.getText();
        var themes = getItems();

        if (title.isEmpty()) {
            ErrorAlert.info("Проект должен иметь название");
            return;
        }
        if (themes.isEmpty()) {
            ErrorAlert.info("Проект должен содержать хотя бы одну тему");
            return;
        }

        App.setProjectTitle(title);
        App.setThemes(themes);

        anotherPage(nextButton, "files-view.fxml");
    }

    @FXML
    public void deleteRow() {
        if (!DeleteAlert.confirm()) return;
        Theme t = table.getSelectionModel().getSelectedItem();
        var themes = table.getItems().filtered(item -> item == t || item.getRoot() == t);
        table.getItems().removeAll(themes);
    }

    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "project-overview.fxml");
    }
}
