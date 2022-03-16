package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.controllers.FilesViewController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ModalFileController extends ModalController<Source> {
    @FXML
    private TextField titleField;
    @FXML
    private TextField uriField;
    @FXML
    private HBox customFields;
    @FXML
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private ComboBox<LiteratureType> typeBox;
    @FXML
    private ComboBox<SourceType> sourceBox;

    private TableViewController<Source> parent;

    @Override
    public void setParent(TableViewController<Source> parent) {
        this.parent = parent;
    }

    public void addFile() throws URISyntaxException {
        if (parent instanceof FilesViewController parent) {
            var source = new Source(new URI(uriField.getText()), sourceBox.getValue(), typeBox.getValue());
            parent.addRow(source);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert sourceBox != null;
        sourceBox.getItems().addAll(
                Arrays.stream(SourceType.values()).filter(type -> type != SourceType.WEB).toList()
        );

        sourceBox.setOnAction(event ->
            customFields.setVisible(sourceBox.getValue() == SourceType.CUSTOM)
        );

        assert typeBox != null;
        typeBox.getItems().addAll(App.getTypes());

        FileChooser.ExtensionFilter extFilter1 = new FileChooser.ExtensionFilter("FB2 files", "*.fb2");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("TXT files", "*.txt");
        FileChooser.ExtensionFilter extFilter3 = new FileChooser.ExtensionFilter("EPUB files", "*.epub");
        fileChooser.getExtensionFilters().addAll(extFilter1, extFilter2, extFilter3);
        uriField.setOnMouseClicked(e -> {
            var stage = addButton.getScene().getWindow();

            try {
                var file = fileChooser.showOpenDialog(stage);
                if (uriField.getText().isEmpty()) titleField.setText(file.getName());
                uriField.setText(file.toURI().toString());
            } catch (NullPointerException ignore) {}

        });
        closeInit();
    }

    @Override
    public void setObject(Source source) {
        uriField.setText(source.getUri().toString());
        sourceBox.getSelectionModel().select(source.getSourceType());
        typeBox.getSelectionModel().select(source.getLitType());
    }
}
