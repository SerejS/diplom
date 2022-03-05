package com.serejs.diplom.desktop.analyze;

import com.serejs.diplom.desktop.Main;
import com.serejs.diplom.desktop.text.container.FragmentMap;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.utils.Settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {
    /**
     * Выравнивание контента относительно процентов
     * @param fragments Обрабатываемые фрагменты
     */
    public static void alignment(FragmentMap fragments) {
        // к-во ключевых слов в каждой теме
        var themeMap = new HashMap<Theme, Long>();

        for (var i : fragments.values()) {
            themeMap.put(i.getTheme(), 0L);
        }

        for (var i : fragments.values()) {
            var th = i.getTheme();
            themeMap.put(th, themeMap.get(th) + i.countKeywords());
        }

        //Вес каждого фрагмента
        var fragmentWeights = new HashMap<String, Double>();
        for (var i : fragments.keySet()) {
            fragmentWeights.put(i, 1.);
        }


        // Состав веса:
        // - К-во ключевых слов относительно количества слов во фрагменте
        // - К-во ключевых слов относительно всех ключевых слов во фрагментах
        // - Уникальность ключевых слов во фрагменте

        for (var i : fragmentWeights.entrySet()) {
            var fragment = fragments.get(i.getKey());
            var weight = i.getValue();

            if (fragment.countKeywords() == 0) {
                continue;
            }

            weight *= (double) fragment.countKeywords() / (double) fragment.countWords();
            weight *= (double) fragment.countKeywords() / (double) themeMap.get(fragment.getTheme());

            var shingleWeight = 1.;
            for (var y : fragments.values()) {
                var th = y.getTheme();
                if (th.equals(fragment.getTheme()) && !y.equals(fragment)) {
                    shingleWeight *= (1 - Shingle.compare(fragment.getContent(), y.getContent()));
                }
            }

            weight *= shingleWeight;

            fragmentWeights.put(i.getKey(), weight);
        }


        //Удаление фрагментов, которые больше ожидаемых процентов
        List<Theme> themes = themeMap.keySet().stream().toList();
        for (int i = 0; i <= themes.size(); i++) {
            for (int k = 0; k <= i && k < themes.size(); k++) {
                Theme theme = themes.get(k);

                var keys = new ArrayList<>(fragments.keySet(theme).stream()
                        .sorted(Comparator.comparingDouble(fragmentWeights::get))
                        .toList());

                while (fragments.percent(theme) > theme.getPercent() + Settings.getDelta() * 0.01) {
                    var worst = keys.get(0);
                    fragments.remove(worst);
                    keys.remove(worst);
                }
            }
        }
    }


    /**
     * Функция выбора темы для текста из предложенных
     *
     * @param content Определяемый текст
     * @param themes  Все преложенные темы
     * @return Выбранная тема
     */
    public static Theme getTheme(String content, List<Theme> themes) {
        //Минимальное количество слов
        var min = Settings.getMinWords();

        //Получение темы с наибольшим количеством совпадением ключевых слов
        Theme resultTheme = null;
        for (Theme theme : themes) {

            var matches = countKeyWords(content, theme.getKeyWords());

            if (matches > min) {
                min = matches;
                resultTheme = theme;
            }
        }

        return resultTheme;
    }


    /**
     * Нормализация слова
     *
     * @param word Изначальное
     * @return Нормализированное
     */
    private static String normalizeWord(String word) {
        if (word.isEmpty()) return word;

        return Lucene
                .getNormalForms(word.toLowerCase(Locale.ROOT).trim())
                .stream().findFirst().orElse("");
    }

    /**
     * ? Добавление синонимов
     * Получение количества ключевых слов в тексте
     *
     * @param content   Подсчитываемый текст
     * @param keyTokens Искомые ключевые слова
     * @return Количество совпадений
     */
    public static long countKeyWords(String content, Set<String> keyTokens) {
        //Игнорируемые слова
        Set<String> stopWords = Analyzer.stopWords();

        //Лист слов в нормальных формах
        List<String> normalWords = Arrays.stream(content.split("[\\pP\\s]"))
                .map(Analyzer::normalizeWord)
                .filter(element -> !element.isEmpty())
                .filter(word -> !stopWords.contains(word))
                .toList();

        //Ключевые слова
        Set<String> keyWords = keyTokens.stream()
                .filter(token -> token.split("\\s").length == 1)
                .map(Analyzer::normalizeWord)
                .collect(Collectors.toSet());

        //Ключевые фразы
        Set<List<String>> keyPhrases = keyTokens.stream()
                .map(token -> Arrays.stream(token.split("\\s")).map(Analyzer::normalizeWord).toList())
                .filter(phrase -> phrase.size() > 1)
                .collect(Collectors.toSet());

        long qty = 0;

        //Посчет ключевых словосочетаний
        for (var i = 0; i < normalWords.size(); i++) {
            checkPhrases:
            for (var phrase : keyPhrases) {
                for (var j = 0; j < phrase.size(); j++) {
                    if (!normalWords.get(i + j).equals(phrase.get(j))) continue checkPhrases;
                }
                qty++;
                break;
            }
        }

        //Добавление количества одиночных ключевых слов в тексте
        qty += normalWords.stream().filter(keyWords::contains).count();

        return qty;
    }


    /**
     * Функция получения из файла стоп-слов
     *
     * @return Множество стоп-слов
     */
    public static HashSet<String> stopWords() {
        HashSet<String> stopWords = new HashSet<>();
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(
                             Objects.requireNonNull(
                                     Main.class.getClassLoader().getResource("stop_ru.txt")
                             ).getFile()
                     ))
        ) {
            stopWords = reader.lines().collect(Collectors.toCollection(HashSet::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stopWords;
    }

}
