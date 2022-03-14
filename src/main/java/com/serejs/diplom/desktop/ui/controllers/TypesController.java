package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TypesController extends TableViewController<LiteratureType> {
    String modalFileName = "modal-type.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var types = App.getTypes();
        if (types != null) table.getItems().addAll(types);

        var title = new TableColumn<LiteratureType, String>("Название типа литературы");
        title.setMinWidth(200);
        title.setCellValueFactory(new PropertyValueFactory<>("title"));

        var main = new TableColumn<LiteratureType, Boolean>("Основная / Дополонительная");
        main.setMinWidth(200);
        main.setCellValueFactory(new PropertyValueFactory<>("main"));


        super.initialize(modalFileName, title, main);
    }

    @FXML
    private void openModal() {
        openModal(modalFileName);
    }

    @FXML
    public void deleteRow() {
        if (!DeleteAlert.confirm()) return;
        LiteratureType t = table.getSelectionModel().getSelectedItem();
        table.getItems().removeAll(t);
        App.getTypes().remove(t);
    }

    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "project-overview.fxml");
    }
}
