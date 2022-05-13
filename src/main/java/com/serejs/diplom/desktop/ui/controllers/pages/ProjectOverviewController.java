package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.controllers.ProjectClientController;
import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import org.apache.http.HttpException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectOverviewController extends TableViewController<Project> {
    @FXML
    private ListView<Project> projectList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjects();

        projectList.setCellFactory(cell -> new ListCell<>() {
            final Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);

                if (project == null || empty) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(project.getTitle());
                    tooltip.setText("Дата создания: " + project.getDate());
                    setTooltip(tooltip);
                }
            }
        });

        //Открытие проекта по двойному щелчку
        projectList.setOnMouseClicked(event -> {
            if (!projectList.getSelectionModel().isEmpty()) deleteButton.setDisable(false);

            var model = projectList.getSelectionModel();
            if (event.getClickCount() != 2 || model.isEmpty()) return;

            var project = model.getSelectedItem();
            try {
                State.getProjectData(project);
            } catch (HttpException | IOException | URISyntaxException e) {
                ErrorAlert.info("Ошибка получения данных выбранного проекта");
                e.printStackTrace();
            }

            anotherPage(addButton, "theme-view.fxml");
        });
    }

    private void loadProjects()  {
        try {
            var projects = State.getProjects();
            projectList.getItems().addAll(projects);
        } catch (IOException | HttpException | URISyntaxException e) {
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

        if (DeleteAlert.confirm()) {
            var project = selectedModel.getSelectedItem();

            try {
                projectList.getItems().remove(project);
                ProjectClientController.deleteProject(project);
            } catch (Exception e) {
                ErrorAlert.info("Ошибка удаления проекта");
                e.printStackTrace();
            }
        }

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
