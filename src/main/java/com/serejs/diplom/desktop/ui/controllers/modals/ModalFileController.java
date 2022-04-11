package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Format;
import com.serejs.diplom.desktop.text.container.FormatSource;
import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.pages.FilesViewController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
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
    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private ComboBox<LiteratureType> typeBox;
    @FXML
    private ComboBox<SourceType> sourceBox;

    @FXML
    private HBox customFields;
    @FXML
    private TextField prev;
    @FXML
    private TextField mid;
    @FXML
    private TextField after;

    private TableViewController<Source> parent;

    @Override
    public void setParent(TableViewController<Source> parent) {
        this.parent = parent;
    }

    public void addFile() throws URISyntaxException {
        if (!(parent instanceof FilesViewController parent)) return;

        var uri = uriField.getText();

        //Валидация полей ввода
        if (uri.isEmpty() || titleField.getText().isEmpty()) {
            ErrorAlert.info("Не указан файл");
            return;
        }
        if (typeBox.getSelectionModel().isEmpty()) {
            ErrorAlert.info("Не указан тип литературы");
            return;
        }

        if (sourceBox.getSelectionModel().isEmpty()) {
            ErrorAlert.info("Не указан тип источника");
            return;
        }

        var fileSource = sourceBox.getValue();
        boolean isFormatSource = fileSource == SourceType.PDF || fileSource == SourceType.CUSTOM;
        if (isFormatSource && (prev.getText().isEmpty() || mid.getText().isEmpty() || after.getText().isEmpty())) {
            ErrorAlert.info("При данном типе литературе необходимо заполнить разделители.");
            return;
        }


        Source source;
        if (isFormatSource){
            var format = new Format(prev.getText(), mid.getText(), after.getText());
            source = new FormatSource(new URI(uri), fileSource, typeBox.getValue(), format);
        } else {
            source = new Source(new URI(uri), fileSource, typeBox.getValue());
        }

        parent.addRow(source);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert sourceBox != null;
        sourceBox.getItems().addAll(
                Arrays.stream(SourceType.values()).filter(type -> type != SourceType.WEB).toList()
        );

        sourceBox.setOnAction(event ->
                customFields.setVisible(
                        sourceBox.getValue() == SourceType.CUSTOM
                        || sourceBox.getValue() == SourceType.PDF
                )
        );

        assert typeBox != null;
        typeBox.getItems().addAll(State.getLitTypes());

        FileChooser.ExtensionFilter extFilter1 = new FileChooser.ExtensionFilter("FB2 files", "*.fb2");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("TXT files", "*.txt");
        FileChooser.ExtensionFilter extFilter3 = new FileChooser.ExtensionFilter("EPUB files", "*.epub");
        FileChooser.ExtensionFilter extFilter4 = new FileChooser.ExtensionFilter("PDF files", "*.pdf");
        fileChooser.getExtensionFilters().addAll(extFilter1, extFilter2, extFilter3, extFilter4);

        uriField.setOnMouseClicked(e -> {
            var stage = addButton.getScene().getWindow();

            try {
                fileChooser.setInitialDirectory(new File("C:\\Users\\serej\\Desktop"));
                var file = fileChooser.showOpenDialog(stage);
                if (uriField.getText().isEmpty()) titleField.setText(file.getName());
                uriField.setText(file.toURI().toString());
            } catch (NullPointerException ignore) {
            }

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
