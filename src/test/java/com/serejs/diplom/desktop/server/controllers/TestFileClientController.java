package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class TestFileClientController {
    private final String testFileText = "testFile";
    private File testFile;
    private Literature literature;


    @Before
    public void beforeEngineTests() {
        //Авторизация
        String rightUsername = System.getenv("rightUsername");
        String rightPassword = System.getenv("rightPassword");

        try {
            UserClientController.auth(rightUsername, rightPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка аутентификации c верными данными");
        }

        //Установка предварительных данных
        try {
            var view = ViewClientController.getViews().get(0);
            State.setView(view);

            var projects = ProjectClientController.getProjects(view);
            State.setProject(projects.get(0));

            var types =  TypeClientController.getTypes(view);
            State.getLitTypes().clear();
            State.getLitTypes().addAll(types);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка получения отображения");
        }

        //Подготовка тестового файла
        try {
            testFile = new File(testFileText + ".txt");
            FileUtils.writeStringToFile(testFile, testFileText, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Ошибка создания тестового файла");
        }

        var output = new File("/output");
        if (!output.exists())
            if (!output.mkdir()) throw new AssertionError("Ошибка создания директории вывода");

        State.setOutputDirectory(output);

        LiteratureType type = State.getLitTypes().get(0);

        var project = State.getProject();
        literature = new Literature(
                -1L,
                testFile.toURI(),
                SourceType.CUSTOM,
                type,
                project
        );
    }

    @Test
    public void fileTest() {
        //Тестирование загрузки файла
        try {
            FileClientController.upload(literature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Тестировнаие скачивания файла
        try {
            FileClientController.download(literature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Проверка целостности содержимого файла
        String downloadedText = null;
        try {
            downloadedText = FileUtils.readFileToString(new File(literature.getUri()), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert downloadedText != null;
        if (downloadedText.equals(testFileText)) return;

        throw new AssertionError("Текст файла модифицирован");
    }


    @After
    public void clearFiles() {
        State.getOutputDirectory().deleteOnExit();
        testFile.deleteOnExit();
    }
}
