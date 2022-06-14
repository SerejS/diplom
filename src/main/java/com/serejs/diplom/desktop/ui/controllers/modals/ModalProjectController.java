package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.server.controllers.ProjectClientController;
import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.controllers.pages.ProjectOverviewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.http.HttpException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ModalProjectController extends ModalController<Project> {
    @FXML
    private TextField title;

    @Override
    public void setParent(TableViewController<Project> parentController) {
        this.parent = parentController;
    }


    public void createProject() throws Exception {
        if (!(parent instanceof ProjectOverviewController parent)) return;

        if (title.getText().isEmpty()) {
            ErrorAlert.info("Название проекта должно иметь хотя бы один символ");
            return;
        }

        var project = new Project(-1L, title.getText(), State.getView(), new Date(System.currentTimeMillis()));
        ProjectClientController.createProject(project);

        parent.addRow(project);
        State.getProjects().add(project);
    }


    @Override
    public void setObject(Project project) {
        //Изменение названия проекта
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeInit();
    }
}
