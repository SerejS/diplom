package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.text.container.Theme;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ThemeController extends RootController implements Initializable {
    @FXML private Button prevButton;
    @FXML private Button addButton;
    @FXML private Button nextButton;
    @FXML private TableView<Theme> themeTable = new TableView<>();

    private Stage modal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        themeTable.setEditable(false);

        var col1 = new TableColumn<Theme, String>("Название темы");
        col1.setMinWidth(200);
        col1.setCellValueFactory(new PropertyValueFactory<>("title"));

        var col2 = new TableColumn<Theme, Double>("Процентное содержание");
        col2.setMinWidth(200);
        col2.setCellValueFactory(new PropertyValueFactory<>("percent"));

        var col3 = new TableColumn<Theme, String>("Ключевые слова");
        col3.setMinWidth(400);
        col3.setCellValueFactory(new PropertyValueFactory<>("keyWords"));

        themeTable.getColumns().addAll(col1, col2, col3);
    }

    @FXML
    private void addTheme() {
        modal = openModal(addButton, this,"modal-theme.fxml");
    }

    public List<Theme> getThemes() {
        return themeTable.getItems();
    }

    public void addRow(Theme theme) {
        themeTable.getItems().add(0, theme);
        modal.close();
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
