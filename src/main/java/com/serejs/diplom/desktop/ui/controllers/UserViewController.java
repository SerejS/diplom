package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.server.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController extends RootController implements Initializable {
    @FXML
    private Button selectButton;
    @FXML
    private Button addButton;
    @FXML
    private ListView<String> listView = new ListView<>();
    private ObservableList list = FXCollections.observableArrayList();
    private String view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load();
    }

    protected void load() {
        list.clear();
        list.addAll(User.getViewTitles());
        listView.getItems().addAll(list);
    }

    @FXML
    private void printSelected(MouseEvent event) {
        this.view = listView.getSelectionModel().getSelectedItem();
        selectButton.setDisable(false);
    }

    @FXML
    private void goNextPage() {
        User.setView(view);
        anotherPage(selectButton, "project-overview.fxml");
    }

    @FXML
    private void addView() {
        openModal(addButton, this, "modal-view.fxml");
    }
}