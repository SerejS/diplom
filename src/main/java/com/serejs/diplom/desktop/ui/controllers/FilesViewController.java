package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.controllers.abstarts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class FilesViewController extends TableViewController<Source> {
    //@FXML private fileChooser = new FileChooser();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var uri = new TableColumn<Source, URI>("Название файла");
        uri.setMinWidth(150);
        uri.setCellValueFactory(new PropertyValueFactory<>("uri"));

        var type = new TableColumn<Source, SourceType>("Тип литературы");
        type.setMinWidth(200);
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        super.initialize(uri, type);
    }

    @FXML
    private void openModal() {
        openModal("modal-file.fxml");
    }

    @FXML
    public void onNextPage() {
        App.addSources(getItems());
        anotherPage(nextButton, "sources-view.fxml");
    }

    @FXML
    public void onPrevPage() {
        anotherPage(prevButton, "theme-view.fxml");
    }
}
