package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.alerts.InfoAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.RootController;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.Processing;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResultController extends RootController implements Initializable {
    @FXML
    private Button finishButton;
    @FXML
    private TextArea area;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        area.setWrapText(true);


        var task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return Processing.getResult();
            }
        };

        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> area.setText(task.getValue()));
        new Thread(task).start();

        try {
            area.setText("Обработка....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goProjectOverview() {
        //Сохранение проекта State.saveProject();
        anotherPage(finishButton, "project-overview.fxml");
    }

    public void saveMdFile() {
        var dir = State.getOutputDirectory();
        try {
            var mdFile = new File(dir.getAbsolutePath() + "/" + State.getProjectTitle() + ".md");
            var writer = new BufferedWriter(new FileWriter(mdFile));
            writer.write(Processing.getMdResult(State.getFragments(), State.getOutputDirectory()));
            writer.flush();
            InfoAlert.info("Вы сохранили результат в файл MarkDown");

            Desktop.getDesktop().open(State.getOutputDirectory());
        } catch (IOException ex) {
            ErrorAlert.info("Ошибка создания файла.");
        } catch (Exception e) {
            ErrorAlert.info("Ошибка получения результата.");
        }
    }
}
