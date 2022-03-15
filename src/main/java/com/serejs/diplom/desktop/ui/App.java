package com.serejs.diplom.desktop.ui;

import com.serejs.diplom.desktop.analyze.Analyzer;
import com.serejs.diplom.desktop.analyze.AutoSummarizer;
import com.serejs.diplom.desktop.loaders.*;
import com.serejs.diplom.desktop.server.Server;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.ui.controllers.AppScene;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import com.serejs.diplom.desktop.utils.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class App extends Application {
    private static String projectTitle;
    private static List<Source> sources;
    private static List<Theme> themes;
    private static Map<Source, Format> customSources = new HashMap<>();
    private static List<LiteratureType> types;
    private static List<GoogleSearchEngine> engines = new LinkedList<>();

    public static void main(String[] args) {
        types = Server.getTypes(1);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Анализ литературы");

        stage.setWidth(1200);
        stage.setMinWidth(800);
        stage.setHeight(600);
        stage.setMinHeight(400);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/ui/hello-view.fxml"));
        AppScene scene = new AppScene(fxmlLoader.load());
        //!!stage.initStyle(StageStyle.UNDECORATED);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public static void createNewProject() {
        sources = new LinkedList<>();
        themes = new LinkedList<>();
    }


    public static void openProject(long id) {
        projectTitle = Server.getProjectTitle(id);
        sources = Server.getSources(id);
        themes = Server.getThemes(id);
        Server.updateSettings(id);
    }


    public static void setProjectTitle(String projectTitle) {
        App.projectTitle = projectTitle;
    }

    public static String getProjectTitle() {
        return projectTitle;
    }


    public static void setThemes(List<Theme> newThemes) {
        themes = newThemes;
    }

    public static List<Theme> getThemes() {
        return themes;
    }


    public static List<LiteratureType> getTypes() {
        return types;
    }


    public static List<Source> getSources() {
        return sources;
    }

    public static void addSources(List<Source> sources) {
        App.sources = sources;
    }

    public static void addSources(List<Source> newSources, Map<Source, Format> newCustomSources) {
        sources.addAll(newSources);
        customSources.putAll(newCustomSources);
    }

    public static void addSources(GoogleSearchEngine engine) throws IOException, URISyntaxException {
        for (Theme theme : themes) {
            sources.addAll(engine.getSources(theme));
        }
    }


    public static List<GoogleSearchEngine> getEngines() {
        return engines;
    }

    public static void addEngine(GoogleSearchEngine engine) {
        engines.add(engine);
    }


    public static String getResult() throws Exception {
        ///Подготовка фрагментов
        var mainFragments = new FragmentMap();
        for (Source source : sources) {
            ContentLoader loader;
            switch (source.getSourceType()) {
                case EPUB -> loader = new EpubLoader();
                case FB2 -> loader = new Fb2Loader();
                case CUSTOM -> loader = new CustomLoader(customSources.get(source));
                case WEB -> loader = new WebLoader();
                default -> {
                    System.err.println("Тип литературы не определен: " + source.getUri());
                    continue;
                }
            }

            loader.load(source.getUri()).forEach((key, content) -> {
                Theme theme = Analyzer.getTheme(content, themes, source.getLitType());
                if (theme == null) return;

                Fragment fragment = new Fragment(content, theme, source.getLitType());
                if (fragment.getConcentration() < Settings.getMinConcentration())
                    fragment = AutoSummarizer.summarize(fragment);

                mainFragments.put(key, fragment);
            });
        }

        mainFragments.recalculateThemes();

        var result = new StringBuilder();

        var totalCount = mainFragments.values().stream().mapToLong(Fragment::countWords).sum();
        result
                .append("Полученное количество слов: ")
                .append(totalCount)
                .append(" на ")
                .append(mainFragments.size())
                .append(" фрагентов")
                .append("\n\n");

        return result.toString();
    }
}
