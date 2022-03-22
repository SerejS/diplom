package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ModalUserView extends ModalController<String> {
    @FXML
    TextField title;

    @Override
    public void setParent(TableViewController<String> parentController) {
        this.parent = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeInit();
    }

    public void addItem() {
        if (title.getText().isEmpty()) {
            ErrorAlert.info("Название не указано");
            return;
        }
        parent.addRow(title.getText());
    }

    @Override
    public void setObject(String t) {
        title.setText(t);
    }
}
