package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectOverviewController extends TableViewController<Project> {
    @FXML
    private ListView<Project> projectList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjects();

        //Открытие проекта по двойному щелчку
        projectList.setOnMouseClicked(event -> {
            if (!projectList.getSelectionModel().isEmpty()) deleteButton.setDisable(false);

            var model = projectList.getSelectionModel();
            if (event.getClickCount() != 2 || model.isEmpty()) return;

            var project = model.getSelectedItem();
            State.getProjectData(project);

            anotherPage(addButton, "theme-view.fxml");
        });
    }

    private void loadProjects()  {
        try {
            var projects = State.getProjects();
            projectList.getItems().addAll(projects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addRow(Project project) {
        projectList.getItems().add(project);
        modal.close();
    }

    @Override
    public void deleteRow() {
        var selectedModel = projectList.getSelectionModel();

        if (DeleteAlert.confirm()) projectList.getItems().remove(selectedModel.getSelectedIndex());

        if (selectedModel.isEmpty()) deleteButton.setDisable(true);
    }

    @FXML
    protected void onCreateProject() {
        openModal("modal-project.fxml");
    }

    @FXML
    protected void openLiteratures() {
        anotherPage(addButton, "types-view.fxml");
    }
}
