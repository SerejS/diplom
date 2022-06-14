package com.serejs.diplom.desktop.utils;

import com.serejs.diplom.desktop.analyze.Analyzer;
import com.serejs.diplom.desktop.loaders.*;
import com.serejs.diplom.desktop.text.container.*;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import static java.util.stream.Collectors.groupingBy;

public class Processing {
    public static String getResult() throws Exception {
        ///Извлечение фрагментов
        var mainFragments = new FragmentMap();
        for (Literature literature : State.getLiteratures()) {
            System.out.println("Начало " + literature.getUri());
            ContentLoader loader;
            switch (literature.getSourceType()) {
                case EPUB -> loader = new EpubLoader();
                case FB2 -> loader = new Fb2Loader();
                case PDF -> loader = new PdfLoader();
                case CUSTOM -> loader = new CustomLoader();
                case WEB -> loader = new WebLoader();
                default -> {
                    System.err.println("Тип литературы не определен: " + literature.getUri());
                    continue;
                }
            }

            var localThemes = State.getThemes().stream()
                    .filter(t -> t.getTypes().contains(literature.getLitType()))
                    .toList();
            if (localThemes.isEmpty()) continue;

            System.out.println("Получение содержания...");

            loader.load(literature);
            var contents = loader.getContent();

            System.out.println("Распределение по темам...");

            for (String key : contents.keySet()) {
                var content = contents.get(key);

                Theme theme = Analyzer.getTheme(content, localThemes);
                if (theme == null) continue;

                System.out.println(literature.getUri() + " " + loader.getContent().size());

                Fragment fragment = new Fragment(content, theme, literature.getLitType(), loader.getAttachments(key));
                if (fragment.getCountWords() > Settings.getMaxWords()) continue;

                if (fragment.getConcentration() < Settings.getMinConcentration()) {
                    //fragment = AutoSummarizer.summarize(fragment);
                    if (fragment.getConcentration() < Settings.getMinConcentration()) continue;
                }
                mainFragments.put(key, fragment);
            }

        }

        System.out.println("Данные получены");

        //Обработка
        mainFragments.recalculateThemes();
        Analyzer.alignment(mainFragments);

        System.out.println("Отфильтровано");

        State.setFragments(mainFragments);

        System.out.println("Начинается вывод фрагментов...");
        return getPlainResult(mainFragments);
    }

    ///Text of MdFile
    public static String getMdResult(FragmentMap mainFragments, File file) throws Exception {
        //Содержание вывода
        var result = new StringBuffer();

        // Группировка в формате темы / типы / фрагменты
        var groupedThemes = mainFragments.keySet().stream().collect(
                groupingBy(k -> mainFragments.get(k).getTheme(),
                        groupingBy(k -> mainFragments.get(k).getType())));

        groupedThemes.forEach((theme, groupedTypes) -> {
            result.append("# Тема: ").append(theme.getTitle()).append('\n');
            mainFragments.length(theme);

            groupedTypes.forEach((type, keys) -> {
                result.append("## Тип литературы: ").append(type.getTitle()).append("\n");
                keys.forEach(key -> {
                    //Подготовка текста для вывода содержания
                    var fragment = mainFragments.get(key);
                    result.append("### ").append(key).append('\n');

                    var concentrateStr = new DecimalFormat("#.0#").format(fragment.getConcentration());
                    result
                            .append("Концентрация ключевых слов: ")
                            .append(concentrateStr)
                            .append('\n')
                            .append(fragment.getContent());

                    //Сохранение приложений
                    fragment.saveAttachments(State.getOutputDirectory());

                });
                result.append("\n");

            });
            result.append("\n");
        });
        FileUtils.writeStringToFile(file, result.toString(), Charset.defaultCharset());
        return result.toString();
    }


    public static String getPlainResult(FragmentMap mainFragments) {
        //Вывод результата
        var result = new StringBuilder();

        result.append("Количество найденных фрагментов: ").append(mainFragments.size()).append("\n\n");

        // Группировка в формате темы / типы / фрагменты
        var groupedThemes = mainFragments.keySet().stream().collect(
                groupingBy(k -> mainFragments.get(k).getTheme(),
                        groupingBy(k -> mainFragments.get(k).getType())));

        groupedThemes.forEach((theme, groupedTypes) -> {
            result.append("Тема: ").append(theme.getTitle()).append("\n");
            result.append("Доля темы: ").append(100 * mainFragments.length(theme) / mainFragments.length()).append("%\n\n");

            groupedTypes.forEach((type, keys) -> {
                result.append("\tТип литературы: ").append(type.getTitle()).append("\n");
                keys.forEach(key -> {
                    //Подготовка текста для вывода содержания
                    var fragment = mainFragments.get(key);

                    var concentrateStr = new DecimalFormat("#.0#").
                            format(fragment.getConcentration() * 100);
                    result.append("\t\t").append(key).append("\n");

                    result.append("\t\t").append("Концентрация ключевых слов: ")
                            .append(concentrateStr).append("%\n");
                    //Сохранение приложений
                    fragment.saveAttachments(State.getOutputDirectory());

                });

            });
            result.append("\n");
        });

        return result.toString();
    }
}