package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController extends TableViewController<String> {
    @FXML
    private Button selectButton;
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
    private void printSelected() {
        this.view = listView.getSelectionModel().getSelectedItem();
        selectButton.setDisable(false);
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
        modal.close();
    }
}