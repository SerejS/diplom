package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController extends TableViewController<String> {
    @FXML
    private Button selectButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ListView<String> listView = new ListView<>();
    private String view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load();
    }

    protected void load() {
        listView.getItems().addAll(User.getViewTitles());
    }

    @FXML
    private void select() {
        this.view = listView.getSelectionModel().getSelectedItem();
        if (this.view == null) return;

        selectButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    @FXML
    private void deleteListItem() {
        if (!DeleteAlert.confirm()) return;

        listView.getItems().remove(view);
        this.view = null;
        selectButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void goNextPage() {
        User.setView(view);
        anotherPage(selectButton, "project-overview.fxml");
    }

    @FXML
    private void openModal() {
        openModal("modal-view.fxml");
    }

    @Override
    public void addRow(String string) {
        listView.getItems().add(string);
        User.addView(string);
        modal.close();
    }
}