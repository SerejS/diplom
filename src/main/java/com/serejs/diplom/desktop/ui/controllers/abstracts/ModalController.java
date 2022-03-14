package com.serejs.diplom.desktop.ui.controllers.abstracts;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public abstract class ModalController<T> extends RootController implements Initializable {
    @FXML
    public Button addButton;
    @FXML
    public Button closeButton;

    protected TableViewController<T> parent;
    private Stage stage;

    protected T obj = null;

    protected void closeInit() {
        closeButton.setOnMouseClicked(e -> {
            close();
        });

    }

    public abstract void setParent(TableViewController<T> parentController);

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);

        stage.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE){
                close();
            }
        });
    }

    public void close() {
        stage.close();
    }


    public abstract void setObject(T t);
}
