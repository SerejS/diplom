package com.serejs.diplom.desktop.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class RootController {
    protected void anotherPage(ButtonBase pageButton, String fileName) {
        try {
            Stage stage = (Stage) pageButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/" + fileName));
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void openModal(Button button, RootController parent, String fileName) {
        Stage root = (Stage) button.getScene().getWindow();

        Stage modalStage = new Stage();
        FXMLLoader modal = new FXMLLoader(getClass().getResource("/ui/" + fileName));
        try {
            modalStage.setScene(new Scene(modal.load()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(root);

        ModalThemeController modalController = modal.getController();
        modalController.setParent(parent);

        modalStage.show();
    }
}
