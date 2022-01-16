package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.server.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitMenuButton;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectOverviewController extends RootController implements Initializable {
    @FXML
    private ListView<String> projectList;
    @FXML
    private SplitMenuButton createButton;
    private ObservableList list = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjects();
    }

    protected void loadProjects()  {
        list.clear();
        list.addAll(User.projectTitles());
        projectList.getItems().addAll(list);
    }

    @FXML
    protected void onCreateProject() {
        anotherPage(createButton, "theme-view.fxml");
    }

}
