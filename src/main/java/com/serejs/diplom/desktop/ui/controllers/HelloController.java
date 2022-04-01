package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController extends RootController implements Initializable {
    @FXML
    private Button login;

    @FXML
    private HBox topPane;

    private double x, y;

    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();

        x = stage.getX() - event.getScreenX();
        y = stage.getY() - event.getScreenY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
       Stage stage = (Stage) topPane.getScene().getWindow();

       stage.setX(event.getScreenX() - x);
       stage.setY(event.getScreenY() - y);
    }


    @FXML
    protected void handleMinAction() {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    protected void handleFullscreenMode() {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }


    @FXML
    protected void handleClose() {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void handleLoginEvent() {
        anotherPage(login, "user-view.fxml");
    }

    @FXML
    protected void registration() {
        System.out.println("Reg click");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}