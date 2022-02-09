package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.containers.Theme;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class ModalThemeController implements Initializable {
    @FXML
    private TextField titleTheme;
    @FXML
    private TextArea textArea;
    @FXML
    private Slider slider;
    @FXML
    private Button addButton;

    private RootController parent;

    public void setParent(RootController parentController) {
        this.parent = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (textArea != null) textArea.setWrapText(true);
    }

    public void addTheme() {
        if (parent instanceof ThemeController) {
            Set<String> keyWords = new HashSet<>(List.of(textArea.getText().split(",")));

            var theme = new Theme(titleTheme.getText(), slider.getValue(), keyWords);

            ((ThemeController) parent).addRow(theme);
        }

    }
}
