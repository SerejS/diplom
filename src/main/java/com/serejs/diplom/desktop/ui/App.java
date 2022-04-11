package com.serejs.diplom.desktop.ui;

import com.serejs.diplom.desktop.ui.controllers.AppScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Анализ литературы");

        stage.setWidth(1200);
        stage.setMinWidth(800);
        stage.setHeight(600);
        stage.setMinHeight(400);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/ui/pages/hello-view.fxml"));
        AppScene scene = new AppScene(fxmlLoader.load());
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
