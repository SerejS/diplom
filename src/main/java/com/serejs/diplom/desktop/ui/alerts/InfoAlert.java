package com.serejs.diplom.desktop.ui.alerts;

import javafx.scene.control.Alert;

public class InfoAlert {
    public static void info(String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);

        var dialog = a.getDialogPane();
        dialog.getStylesheets().add(
                String.valueOf(InfoAlert.class.getResource("/ui/style/alert.css")));
        dialog.getStyleClass().add("dialog");

        a.setTitle("Сохранение");
        a.setHeaderText(text);
        a.show();
    }
}
