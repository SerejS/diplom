package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.controllers.UserClientController;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.alerts.InfoAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegisterController extends RootController {
    @FXML
    TextField login;
    @FXML
    TextField pass;
    @FXML
    TextField repeat;

    @FXML
    Button prevButton;
    @FXML
    Button regButton;

    @FXML
    private void goPrevPage() {
        anotherPage(prevButton, "login-view.fxml");
    }

    @FXML
    private void register() {
        var username = this.login.getText();
        var password = this.pass.getText();
        var repeat = this.repeat.getText();

        var minLength = 5;
        if (username.length() < minLength || password.length() < minLength) {
            ErrorAlert.info("Поля должны содержать как минимум 5 символов!");
            return;
        }

        if (!password.equals(repeat)) {
            ErrorAlert.info("Пароли должны совпадать!");
            return;
        }

        try {
            UserClientController.register(username, password);
        } catch (Exception e) {
            ErrorAlert.info("Ошибка регистрации");
            e.printStackTrace();
            return;
        }

        InfoAlert.info("Аккаунт успешно создан");
        anotherPage(regButton, "login-view.fxml");
    }
}
