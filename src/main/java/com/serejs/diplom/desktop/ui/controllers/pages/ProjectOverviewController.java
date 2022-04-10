package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.ServerClient;
import com.serejs.diplom.desktop.ui.App;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ProjectOverviewController extends TableViewController<String> {
    @FXML
    private ListView<String> projectList;
    @FXML
    private Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjects();
        projectList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                var project = projectList.getSelectionModel().getSelectedItem();

                //Получить идентификатор проекта
                //App.openProject(project.length());
                anotherPage(createButton, "theme-view.fxml");
            }

            if (!projectList.getSelectionModel().isEmpty()) {
                deleteButton.setDisable(false);
            }
        });
    }

    protected void loadProjects()  {
        var projects = new HashMap<Long, String>();

        try {
            projects = ServerClient.getProjects(State.getViewID());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Добавить получение идентификаторов, чтобы можно было сделать следующий запрос.
        projectList.getItems().addAll(projects.values());
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
