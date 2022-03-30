package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.TypesController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ModalTypeController extends ModalController<LiteratureType> {
    @FXML
    private TextField title;
    @FXML
    private CheckBox main;

    private TableViewController<LiteratureType> parent;

    @Override
    public void setParent(TableViewController<LiteratureType> parent) {
        this.parent = parent;
    }

    public void addType() {
        if (parent instanceof TypesController parent) {
            if (title.getText().isEmpty()) {
                ErrorAlert.info("Название типа литературы должно иметь хотя бы один символ");
                return;
            }

            var type = new LiteratureType(title.getText(), main.isSelected());

            parent.addRow(type);
            App.getTypes().add(type);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeInit();
    }

    @Override
    public void setObject(LiteratureType type) {
        this.title.setText(type.getTitle());
        this.main.setSelected(type.isMain());
    }
}
