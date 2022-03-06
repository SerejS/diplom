package com.serejs.diplom.desktop.ui;

import com.serejs.diplom.desktop.analyze.Analyzer;
import com.serejs.diplom.desktop.loaders.*;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.ui.controllers.AppScene;
import com.serejs.diplom.desktop.ui.controllers.ProjectOverviewController;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class App extends Application {
    private static List<Source> sources;
    private static List<Theme> themes;
    private static Map<Source, Format> customSources;

    public static void createNewProject() {
        sources = new LinkedList<>();
        themes = new LinkedList<>();
    }

    public static void setThemes(List<Theme> newThemes) {
        themes = newThemes;
    }

    public static void addSources(List<Source> newSources, Map<Source, Format> newCustomSources) {
        sources.addAll(newSources);
        customSources.putAll(newCustomSources);
    }

    public static void addSources(GoogleSearchEngine engine) throws IOException, URISyntaxException {
        for(Theme theme: themes) {
            sources.addAll(engine.getSources(theme));
        }
    }

    public static String getResult() throws Exception {
        ///Подготовка фрагментов
        var mainFragments = new FragmentMap();
        for (Source source : sources) {
            ContentLoader loader;
            switch (source.getType()) {
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
                Theme theme = Analyzer.getTheme(content, themes);
                if (theme == null) return;

                Fragment fragment = new Fragment(content, theme);
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

        //Приведение количества фрагментов к конечному количеству
        Analyzer.alignment(mainFragments);

        int totalCount2 = mainFragments.values().stream().mapToInt(Fragment::countWords).sum();

        mainFragments.forEach((key, f) ->
                result
                        .append(f.theme().getTitle()).append(" ")
                        .append(key).append(" ~")
                        .append(100 * f.countWords() / totalCount2).append("%")
                        .append('\n')
        );


        result
                .append("Конечное количество слов: ").append(totalCount2)
                .append(" на ")
                .append(mainFragments.size()).append(" фрагентов")
                .append("\n");

        mainFragments.getThemes().forEach(t ->
                result
                        .append(t.getTitle()).append(" ")
                        .append(t.getPercent()).append(" % ")
                        .append("\\")
                        .append((double) mainFragments.countWords(t) / totalCount2).append(" %")
                        .append('\n')
        );

        return result.toString();
    }

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
