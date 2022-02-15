package com.serejs.diplom.desktop.ui.controllers.abstarts;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
    protected String modalFileName;
    @FXML
    protected TableView<T> table = new TableView<>();
    protected ModalController<T> modal;


    @SafeVarargs
    public final void initialize(String modalFileName, TableColumn<T, ?>... columns) {
        this.modalFileName = modalFileName;

        table.setEditable(false);
        table.getColumns().addAll(columns);

        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    openModal(modalFileName, row.getItem());
                }
            });
            return row ;
        });

    }

    public List<T> getItems() {
        return table.getItems();
    }

    @FXML
    protected void openModal(String fileName) {
        Stage root = (Stage) addButton.getScene().getWindow();

        Stage modalStage = new Stage();
        FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("/ui/modals/" + fileName));
        try {
            modalStage.setScene(new Scene(modalLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(root);

        modal = modalLoader.getController();
        modal.setParent(this);
        modal.setStage(modalStage);

        modalStage.show();
    }

    @FXML
    protected void openModal(String fileName, T t) {
        openModal(fileName);
        modal.setObject(t);
    }
    @FXML
    public void addRow(T t) {
        table.getItems().add(table.getItems().size(), t);
        modal.close();
    }

    @FXML
    public void deleteRow() {
        T t = table.getSelectionModel().getSelectedItem();
        table.getItems().removeAll(t);
    }
}
