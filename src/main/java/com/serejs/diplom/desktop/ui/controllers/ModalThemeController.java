package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.text.container.Theme;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ModalThemeController implements Initializable {
    @FXML
    private TextField titleTheme;
    @FXML
    private TextArea textArea;
    @FXML
    private Slider slider;
    @FXML
    private ComboBox<Theme> themeBox;

    private RootController parent;

    public void setParent(RootController parentController) {
        this.parent = parentController;

        if (themeBox != null && parent instanceof ThemeController controller) {
            themeBox.getItems().addAll(controller.getThemes());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (textArea != null) textArea.setWrapText(true);
    }

    public void addTheme() {
        if (parent instanceof ThemeController) {
            Set<String> keyWords = Arrays.stream(textArea.getText().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            var theme = new Theme(themeBox.getValue(), titleTheme.getText(), slider.getValue(), keyWords);

            ((ThemeController) parent).addRow(theme);
        }

    }
}
