package com.serejs.diplom.desktop.ui.controllers;

import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitMenuButton;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectOverviewController extends TableViewController<String> {
    @FXML
    private ListView<String> projectList;
    @FXML
    private SplitMenuButton createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjects();
        projectList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                var project = projectList.getSelectionModel().getSelectedItem();

                App.openProject(project.length());
                anotherPage(createButton, "theme-view.fxml");
            }
        });
    }

    protected void loadProjects()  {
        projectList.getItems().addAll(User.projectTitles());
    }

    @Override
    public void deleteRow() {
        if (DeleteAlert.confirm()) {
            projectList.getItems().remove(projectList.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    protected void onCreateProject() {
        App.createNewProject();
        anotherPage(createButton, "theme-view.fxml");
    }

}
