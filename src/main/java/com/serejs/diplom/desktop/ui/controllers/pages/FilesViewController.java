package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Format;
import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FilesViewController extends TableViewController<Source> {
    @FXML
    private final String modalFileName = "modal-file.fxml";
    @Getter
    private final Map<Source, Format> customSources = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var sources = State.getSources();
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
        var sources = getItems();
        if (sources.isEmpty()) {
            ErrorAlert.info("Проект должен содержать хотя бы один источник");
            return;
        }

        State.setSources(sources);
        anotherPage(nextButton, "web-view.fxml");
    }

    @FXML
    public void onPrevPage() {
        State.setSources(getItems());
        anotherPage(prevButton, "theme-view.fxml");
    }
}
