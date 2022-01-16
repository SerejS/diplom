package com.serejs.diplom.desktop.ui;

import com.serejs.diplom.desktop.ui.controllers.AppScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Анализ литературы");

        stage.setWidth(1000);
        stage.setMinWidth(800);
        stage.setHeight(600);
        stage.setMinHeight(400);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/ui/hello-view.fxml"));
        AppScene scene = new AppScene(fxmlLoader.load());
        //!!stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);
        stage.show();
    }
}
