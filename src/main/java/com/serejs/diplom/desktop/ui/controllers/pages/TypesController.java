package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.controllers.TypeClientController;
import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.http.HttpException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class TypesController extends TableViewController<LiteratureType> {
    String modalFileName = "modal-type.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var types = State.getLitTypes();
        if (types != null) table.getItems().addAll(types);

        var title = new TableColumn<LiteratureType, String>("Название типа литературы");
        title.setMinWidth(500);
        title.setCellValueFactory(new PropertyValueFactory<>("title"));

        var main = new TableColumn<LiteratureType, Boolean>("Основная / Дополонительная");
        main.setMinWidth(500);
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
        if (table.getItems().size() < 2) {
            ErrorAlert.info("Должен существовать хотя бы один тип литературы.");
            return;
        }

        LiteratureType t = table.getSelectionModel().getSelectedItem();

        try {
            TypeClientController.deleteType(t);
            table.getItems().removeAll(t);
            State.getLitTypes().remove(t);
        } catch (HttpException | IOException | URISyntaxException e) {
            ErrorAlert.info("Ошибка удаления типа литературы");
            e.printStackTrace();
        }

    }

    @FXML
    protected void goPrevPage() {
        anotherPage(prevButton, "project-overview.fxml");
    }
}
