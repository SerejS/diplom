package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ProjectOverviewController extends TableViewController<String> {
    @FXML
    private ListView<Project> projectList;
    @FXML
    private Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjects();
        projectList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                var project = projectList.getSelectionModel().getSelectedItem();

                //Получить идентификатор проекта
                State.getProjectData(project.getId());
                anotherPage(createButton, "theme-view.fxml");
            }

            if (!projectList.getSelectionModel().isEmpty()) {
                deleteButton.setDisable(false);
            }
        });
    }

    protected void loadProjects()  {
        var projects = new LinkedList<Project>();

        try {
            projects = State.getProjects();
        } catch (IOException e) {
            e.printStackTrace();
        }

        projectList.getItems().addAll(projects);
    }

    @Override
    public void deleteRow() {
        if (DeleteAlert.confirm()) {
            projectList.getItems().remove(projectList.getSelectionModel().getSelectedIndex());
        }

        if (projectList.getSelectionModel().isEmpty()) deleteButton.setDisable(true);
    }

    @FXML
    protected void onCreateProject() {
        State.createNewProject();
        anotherPage(createButton, "theme-view.fxml");
    }

    @FXML
    protected void openLiteratures() {
        anotherPage(createButton, "types-view.fxml");
    }
}
