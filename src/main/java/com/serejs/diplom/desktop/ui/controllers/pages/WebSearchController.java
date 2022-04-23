package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WebSearchController extends TableViewController<GoogleSearchEngine> {
    private final String modalFileName = "modal-web.fxml";

    @FXML
    private Button prevButton;
    @FXML
    private Button analiseButton;

    @FXML
    private Button rpdButton;
    @FXML
    private Button clearButton;
    @FXML
    private final FileChooser fileChooser = new FileChooser();
    private File rpdFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var engines = State.getEngines();
        table.getItems().addAll(engines);

        var cx = new TableColumn<GoogleSearchEngine, String>("CX");
        cx.setMinWidth(300);
        cx.setCellValueFactory(new PropertyValueFactory<>("cx"));

        var key = new TableColumn<GoogleSearchEngine, String>("Key");
        key.setMinWidth(300);
        key.setCellValueFactory(new PropertyValueFactory<>("key"));

        var litType = new TableColumn<GoogleSearchEngine, LiteratureType>("Тип литературного источника");
        litType.setMinWidth(300);
        litType.setCellValueFactory(new PropertyValueFactory<>("type"));

        super.initialize(modalFileName, cx, key, litType);

        rpdButton.setOnMouseClicked(mouseEvent -> {
            if (rpdFile != null) {
                try {
                    Desktop.getDesktop().open(rpdFile);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            var stage = rpdButton.getScene().getWindow();
            var file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                fileChooser.setInitialDirectory(file.getParentFile());
                rpdFile = file;
                clearButton.setDisable(false);
            }
        });

    }

    @FXML
    private void clearRpd() {
        rpdFile = null;
        clearButton.setDisable(true);
    }

    @FXML
    private void openModal() {
        var containsAllTypes = table.getItems()
                .stream().map(GoogleSearchEngine::getType)
                .collect(Collectors.toSet()).containsAll(State.getLitTypes());

        if (containsAllTypes)
            ErrorAlert.info("Добавлены поисковые движки ко всем типам литерартуры");
        else
            openModal(modalFileName);

    }

    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "files-view.fxml");
    }

    @FXML
    protected void goSettingsPage() {
        anotherPage(analiseButton, "settings-view.fxml");
    }
}
