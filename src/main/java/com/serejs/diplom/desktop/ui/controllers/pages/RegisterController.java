package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.alerts.InfoAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.serejs.diplom.desktop.utils.Encrypt.encrypt;

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
        var login = this.login.getText();
        var pass = this.pass.getText();
        var repeat = this.repeat.getText();

        var minLength = 5;
        if (login.length() < minLength || pass.length() < minLength) {
            ErrorAlert.info("Поля должны содержать как минимум 5 символов!");
            return;
        }

        if (!pass.equals(repeat)) {
            ErrorAlert.info("Пароли должны совпадать!");
            return;
        }

        String salt = null;
        try {
            var encrypted = encrypt(pass);
            pass = encrypted.getKey();
            salt = encrypted.getValue();
        } catch (Exception e) {
            ErrorAlert.info("Ошибка шифрования пароля");
            return;
        }

        ///Тут должен быть запрос к серверу...

        InfoAlert.info("Аккаунт успешно создан");
        anotherPage(regButton, "login-view.fxml");
    }
}
