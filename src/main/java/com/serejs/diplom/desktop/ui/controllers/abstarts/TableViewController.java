package com.serejs.diplom.desktop.ui.controllers.abstarts;

import com.serejs.diplom.desktop.ui.controllers.modals.ModalController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public abstract class TableViewController<T> extends RootController implements Initializable {
    @FXML
    protected Button prevButton;
    @FXML
    protected Button addButton;
    @FXML
    protected Button nextButton;
    @FXML
    protected TableView<T> table = new TableView<>();
    protected Stage modal;


    @SafeVarargs
    public final void initialize(TableColumn<T, ?>... columns) {
        table.setEditable(false);
        table.getColumns().addAll(columns);
    }

    public List<T> getItems() {
        return table.getItems();
    }

    @FXML
    protected void openModal(String fileName) {
        Stage root = (Stage) addButton.getScene().getWindow();

        modal = new Stage();
        FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("/ui/modals/" + fileName));
        try {
            modal.setScene(new Scene(modalLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(root);

        ModalController<T> modalController = modalLoader.getController();
        modalController.setParent(this);

        modal.show();
    }

    @FXML
    public void addRow(T t) {
        table.getItems().add(table.getItems().size(), t);
        modal.close();
    }
}
