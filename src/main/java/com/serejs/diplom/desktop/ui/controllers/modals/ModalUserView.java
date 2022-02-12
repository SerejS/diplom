package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.ui.controllers.abstarts.TableViewController;
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
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void addItem() {
        parent.addRow(title.getText());
    }
}
