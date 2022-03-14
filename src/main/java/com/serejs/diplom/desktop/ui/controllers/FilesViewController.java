package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class FilesViewController extends TableViewController<Source> {
    @FXML
    private final String modalFileName = "modal-file.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var sources = App.getSources();
        table.getItems().addAll(sources.stream().filter(s -> s.getSourceType() != SourceType.WEB).toList());

        var uri = new TableColumn<Source, URI>("Название файла");
        uri.setMinWidth(300);
        uri.setCellValueFactory(new PropertyValueFactory<>("uri"));

        var sourceType = new TableColumn<Source, SourceType>("Тип источника");
        sourceType.setMinWidth(200);
        sourceType.setCellValueFactory(new PropertyValueFactory<>("sourceType"));

        var litType = new TableColumn<Source, LiteratureType>("Тип литературы");
        litType.setMinWidth(200);
        litType.setCellValueFactory(new PropertyValueFactory<>("litType"));

        super.initialize(modalFileName, uri, sourceType, litType);
    }

    @FXML
    private void openModal() {
        openModal(modalFileName);
    }

    @FXML
    public void onNextPage() {
        App.addSources(getItems());
        anotherPage(nextButton, "web-view.fxml");
    }

    @FXML
    public void onPrevPage() {
        App.addSources(getItems());
        anotherPage(prevButton, "theme-view.fxml");
    }
}
