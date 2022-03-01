package com.serejs.diplom.desktop.ui.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DeleteAlert {
    public static boolean confirm() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Удаление");
        a.setHeaderText("Вы точно желаете удалить?");
        var choice = a.showAndWait();

        return choice.isPresent() && choice.get() == ButtonType.OK;
    }
}
