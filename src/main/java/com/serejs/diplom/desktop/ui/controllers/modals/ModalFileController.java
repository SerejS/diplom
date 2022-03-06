package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.ui.controllers.FilesViewController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModalFileController extends ModalController<Source> {
    @FXML
    private TextField uriField;
    @FXML
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private ComboBox<SourceType> sourceBox;

    private TableViewController<Source> parent;

    @Override
    public void setParent(TableViewController<Source> parent) {
        this.parent = parent;

        if (sourceBox != null) {
            sourceBox.getItems().addAll(SourceType.values());
        }
    }

    public void addFile() throws URISyntaxException {
        if (parent instanceof FilesViewController parent) {
            var source = new Source(new URI(uriField.getText()), sourceBox.getValue());
            parent.addRow(source);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uriField.setOnMouseClicked(e -> {
            var stage = addButton.getScene().getWindow();

            try {
                uriField.setText(fileChooser.showOpenDialog(stage).toURI().toString());
            } catch (NullPointerException ignore) {}

        });
        closeInit();
    }

    @Override
    public void setObject(Source source) {
        uriField.setText(source.getUri().toString());
        sourceBox.getSelectionModel().select(source.getType());
    }
}
