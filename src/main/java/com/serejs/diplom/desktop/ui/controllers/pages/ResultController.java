package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.controllers.FileClientController;
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
                var literatures = com.serejs.diplom.desktop.ui.states.State.getLiteratures();

                //Получение файлов литературы с сервера, которых нет на компьютере
                for (var lit : literatures) {
                    if (lit.getId() != -1L)
                        FileClientController.download(lit);
                }

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
        try {
            State.saveProjectData();
        } catch (Exception e) {
            ErrorAlert.info("Ошибка сохранения данных проекта");
        }
        anotherPage(finishButton, "project-overview.fxml");
    }

    public void saveMdFile() {
        var dir = State.getOutputDirectory();
        try {
            var mdFile = new File(dir.getAbsolutePath() + "/" + State.getProject().getTitle() + ".md");
            var writer = new BufferedWriter(new FileWriter(mdFile));
            writer.write(Processing.getMdResult(State.getFragments(), mdFile));
            writer.flush();
            InfoAlert.info("Вы сохранили результат в файл MarkDown");

            Desktop.getDesktop().open(State.getOutputDirectory());
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorAlert.info("Ошибка создания файла.");
        } catch (Exception e) {
            e.printStackTrace();
            ErrorAlert.info("Ошибка получения результата.");
        }
    }
}
