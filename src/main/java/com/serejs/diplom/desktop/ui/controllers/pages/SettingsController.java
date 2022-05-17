package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends RootController implements Initializable {
    @FXML
    TextField minFragments;
    @FXML
    TextField delta;
    @FXML
    TextField minWords;
    @FXML
    TextField maxWords;
    @FXML
    TextField minKeyWords;
    @FXML
    TextField maxMicroRange;
    @FXML
    TextField minConcentration;
    @FXML
    CheckBox referring;
    @FXML
    CheckBox attachments;

    @FXML
    Button nextButton;
    @FXML
    Button prevButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        minFragments.setText(String.valueOf(Settings.getMinimalFragmentsPerTheme()));
        delta.setText(String.valueOf(Settings.getDelta()));
        minWords.setText(String.valueOf(Settings.getMinWords()));
        maxWords.setText(String.valueOf(Settings.getMaxWords()));
        minKeyWords.setText(String.valueOf(Settings.getMinKeyNGrams()));
        maxMicroRange.setText(String.valueOf(Settings.getMaxMicroRange()));
        minConcentration.setText(String.valueOf(Settings.getMinConcentration()));
        referring.setSelected(Settings.isAutoExtracting());
        attachments.setSelected(Settings.isSaveAttachments());
    }

    private void saveState() throws NumberFormatException {
        Settings.setMinimalFragmentsPerTheme(Short.parseShort(minFragments.getText()));
        Settings.setDelta(Short.parseShort(delta.getText()));
        Settings.setMinWords(Long.parseLong(minWords.getText()));
        Settings.setMaxWords(Long.parseLong(maxWords.getText()));
        Settings.setMinKeyNGrams(Long.parseLong(minKeyWords.getText()));
        Settings.setMaxMicroRange(Long.parseLong(maxMicroRange.getText()));
        Settings.setMinConcentration(Float.parseFloat(minConcentration.getText()));
        Settings.setAutoExtracting(referring.isSelected());
        Settings.setSaveAttachments(attachments.isSelected());
    }

    @FXML
    private void goNextPage() {
        try {
            saveState();
        } catch (NumberFormatException ex) {
            ErrorAlert.info("Неправльный формат одного из полей.");
            return;
        }

        anotherPage(nextButton, "result-view.fxml");
    }

    @FXML
    protected void goPrevPage() {
        try {
            saveState();
        } catch (NumberFormatException ex) {
            ErrorAlert.info("Неправльный формат одного из полей.");
            return;
        }

        anotherPage(prevButton, "web-view.fxml");
    }

    @FXML
    public void closeProject() {
        State.clearProject();
        anotherPage(prevButton, "project-overview.fxml");
    }
}
