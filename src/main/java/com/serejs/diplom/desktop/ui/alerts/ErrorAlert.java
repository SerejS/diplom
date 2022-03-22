package com.serejs.diplom.desktop.ui.alerts;

import javafx.scene.control.Alert;

public class ErrorAlert {
    public static void info(String text) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Ошибка");
        a.setHeaderText(text);
        a.show();
    }
}
