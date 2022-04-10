package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.ServerClient;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        try {
            listView.getItems().addAll(ServerClient.getViews().values());
        } catch (Exception e) {
            System.err.println("Ошибка получения отображений с сервера.");
        }
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
        State.setViewID((long) 1);
        anotherPage(selectButton, "project-overview.fxml");
    }

    @FXML
    private void openModal() {
        openModal("modal-view.fxml");
    }

    @Override
    public void addRow(String string) {
        listView.getItems().add(string);
        ServerClient.addView(string);
        modal.close();
    }
}