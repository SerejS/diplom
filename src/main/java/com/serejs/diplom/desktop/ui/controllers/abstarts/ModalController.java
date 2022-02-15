package com.serejs.diplom.desktop.ui.controllers.abstarts;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class ModalController<T> extends RootController implements Initializable {
    @FXML
    public Button addButton;

    protected TableViewController<T> parent;
    private Stage stage;

    public abstract void setParent(TableViewController<T> parentController);

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        stage.close();
    }


    public abstract void setObject(T t);
}
