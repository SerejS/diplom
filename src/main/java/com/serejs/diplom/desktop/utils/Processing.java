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
        for (Source source : State.getSources()) {
            ContentLoader loader;
            switch (source.getSourceType()) {
                case EPUB -> loader = new EpubLoader();
                case FB2 -> loader = new Fb2Loader();
                case PDF -> loader = new PdfLoader();
                case CUSTOM -> loader = new CustomLoader();
                case WEB -> loader = new WebLoader();
                default -> {
                    System.err.println("Тип литературы не определен: " + source.getUri());
                    continue;
                }
            }

            var localThemes = State.getThemes().stream()
                    .filter(t -> t.getTypes().contains(source.getLitType()))
                    .toList();
            if (localThemes.isEmpty()) continue;

            loader.load(source);
            var contents = loader.getContent();

            for (String key : contents.keySet()) {
                var content = contents.get(key);

                Theme theme = Analyzer.getTheme(content, localThemes);
                if (theme == null) continue;

                System.out.println(source.getUri() + " " + loader.getContent().size());

                Fragment fragment = new Fragment(content, theme, source.getLitType(), loader.getAttachments(key));
                if (fragment.getCountWords() > Settings.getMaxWords()) continue;

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

        State.setFragments(mainFragments);

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