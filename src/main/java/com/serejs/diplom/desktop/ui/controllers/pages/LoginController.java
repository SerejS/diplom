package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.controllers.UserClientController;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends RootController implements Initializable {
    @FXML
    private Button login;
    @FXML
    private Button register;

    @FXML
    private TextField username;
    @FXML
    private TextField password;


    @FXML
    private HBox topPane;

    private double x, y;

    //Функции работы с аппбаром
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


    //Контроллеры экрана
    @FXML
    protected void handleLoginEvent() {
        var usernameValue = username.getText();
        var passwordValue = password.getText();

        if (usernameValue.length() < 5 || passwordValue.length() < 5) {
            ErrorAlert.info("Каждое из полей должно иметь хотя бы 5 символов");
            return;
        }

        try {
            UserClientController.auth(usernameValue, passwordValue);
            anotherPage(login, "user-view.fxml");
        } catch (Exception e) {
            ErrorAlert.info("Неверные данные");
        }
    }

    @FXML
    protected void handleRegisterEvent() {
       anotherPage(register, "register-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        password.setOnKeyPressed((keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) handleLoginEvent();
        }));
    }
}