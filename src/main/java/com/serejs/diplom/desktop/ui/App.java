package com.serejs.diplom.desktop.ui;

import com.serejs.diplom.desktop.analyze.Analyzer;
import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.loaders.*;
import com.serejs.diplom.desktop.server.Server;
import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.ui.controllers.AppScene;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import com.serejs.diplom.desktop.utils.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class App extends Application {
    private static String projectTitle;
    private static List<Source> sources;
    private static List<Theme> themes;
    private static Map<Source, Format> customSources = new HashMap<>();
    private static List<LiteratureType> types;
    private static List<GoogleSearchEngine> engines = new LinkedList<>();
    private static File outputDirectory;
    @Getter
    private static FragmentMap fragments;

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

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/ui/pages/hello-view.fxml"));
        AppScene scene = new AppScene(fxmlLoader.load());
        //!!stage.initStyle(StageStyle.UNDECORATED);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public static void createNewProject() {
        sources = new LinkedList<>();
        themes = new LinkedList<>();
        selectResultDir();
    }


    public static void openProject(long id) {
        projectTitle = Server.getProjectTitle(id);
        sources = Server.getSources(id);
        themes = Server.getThemes(id);
        Server.updateSettings(id);
    }

    public static void saveProject() {
        User.addProject(projectTitle);
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

    public static void remove(LiteratureType type) {
        //Сделать получение из БД
        var defaultType = new LiteratureType("Общий", true);

        types.remove(type);
        sources.stream().filter(s -> s.getLitType() == type).forEach(s -> s.setLitType(defaultType));
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


    public static File getOutputDirectory() {
        return outputDirectory;
    }

    public static void selectResultDir() {
        var chooser = new DirectoryChooser();
        try {
            var dir = chooser.showDialog(null);
            if (dir.isDirectory()) outputDirectory = dir;
        } catch (NullPointerException ignored) {
        }
    }

    public static String getResult() throws Exception {
        ///Извлечение фрагментов
        var mainFragments = new FragmentMap();
        for (Source source : getSources()) {
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

            var localThemes = themes.stream()
                    .filter(t -> t.getTypes().contains(source.getLitType()))
                    .toList();
            if (localThemes.isEmpty()) continue;

            loader.load(source.getUri());
            var contents = loader.getContent();

            for (String key : contents.keySet()) {
                var content = contents.get(key);

                Theme theme = Analyzer.getTheme(content, localThemes);
                if (theme == null) continue;

                System.out.println(source.getUri() + " " + loader.getContent().size());

                Fragment fragment = new Fragment(content, theme, source.getLitType(), loader.getAttachments(key));
                if (fragment.getConcentration() < Settings.getMinConcentration()) {
                    //fragment = AutoSummarizer.summarize(fragment);
                    if (fragment.getConcentration() < Settings.getMinConcentration()) continue;
                }
                mainFragments.put(key, fragment);
            }

        }

        //Обработка
        mainFragments.recalculateThemes();
        Analyzer.alignment(mainFragments);

        fragments = mainFragments;

        return getPlainResult(mainFragments);
    }

    ///Text of MdFile
    public static String getMdResult(FragmentMap mainFragments, File file) throws Exception {
        //Вывод результата
        var result = new StringBuilder();

        // Группировка в формате темы / типы / фрагменты
        var groupedThemes = mainFragments.keySet().stream().collect(
                groupingBy(k -> mainFragments.get(k).getTheme(),
                        groupingBy(k -> mainFragments.get(k).getType())));

        groupedThemes.forEach((theme, groupedTypes) -> {
            result.append("# Тема: ").append(theme.getTitle()).append('\n');

            groupedTypes.forEach((type, keys) -> {
                result.append("## Тип литературы: ").append(type.getTitle()).append("\n");
                keys.forEach(key -> {
                    //Подготовка текста для вывода содержания
                    var fragment = mainFragments.get(key);
                    result.append("### ").append(key).append('\n');
                    result.append("Концентрация ключевых слов: ").append(fragment.getConcentration()).append('\n');
                    result.append(fragment.getContent()).append('\n');

                    //Сохранение приложений
                    fragment.saveAttachments(outputDirectory);

                });
                result.append("\n");

            });
            result.append("\n");
        });
        FileUtils.writeStringToFile(file, result.toString());
        return result.toString();
    }


    public static String getPlainResult(FragmentMap mainFragments) {
        //Вывод результата
        var result = new StringBuilder();

        result.append("Количество найденных фрагментов: ").append(mainFragments.size()).append('\n');

        // Группировка в формате темы / типы / фрагменты
        var groupedThemes = mainFragments.keySet().stream().collect(
                groupingBy(k -> mainFragments.get(k).getTheme(),
                        groupingBy(k -> mainFragments.get(k).getType())));

        groupedThemes.forEach((theme, groupedTypes) -> {
            result.append(theme.getTitle()).append('\n');

            groupedTypes.forEach((type, keys) -> {
                result.append("Тип литературы: ").append(type.getTitle()).append("\n");
                keys.forEach(key -> {
                    //Подготовка текста для вывода содержания
                    var fragment = mainFragments.get(key);
                    result.append(fragment.getType()).append(" / ").append(key);
                    result.append(": ").append(fragment.getConcentration()).append('\n');
                    result.append(fragment.getContent(), 0, 200).append('\n');

                    //Сохранение приложений
                    fragment.saveAttachments(outputDirectory);

                });
                result.append("\n");

            });
            result.append("\n");
        });

        return result.toString();
    }

    ///Text of MdFile
    public static String getMdResult() throws Exception {
        return "#Название темы....";
    }
}
