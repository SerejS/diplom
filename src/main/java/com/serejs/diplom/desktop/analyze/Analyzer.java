package com.serejs.diplom.desktop.analyze;

import com.serejs.diplom.desktop.Main;
import com.serejs.diplom.desktop.text.container.Fragment;
import com.serejs.diplom.desktop.text.container.FragmentMap;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.utils.Settings;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {
    /**
     * Выравнивание найденного материала
     *
     * @param fragments Обрабатываемый материал
     */
    public static void alignment(FragmentMap fragments) {
        var themes = fragments.getThemes().stream().toList();
        for (int i = 0; i <= themes.size(); i++) {
            for (int k = 0; k <= i && k < themes.size(); k++) {
                localAlignment(fragments, themes.get(k));
            }
        }
    }


    /**
     * Выравнивание процентного содержания относительно каждой темы
     */
    private static void localAlignment(FragmentMap fragments, Theme theme) {
        int totalWords = fragments.values().stream().mapToInt(Fragment::countWords).sum();
        double wordsPerPercent = Math.min((double) Settings.getMaxWords() / 100, (double) totalWords / 100);

        //Реальное процентное содержание текстов данной темы
        double percents = fragments.countWords(theme) / wordsPerPercent;
        if (percents < theme.getPercent() + Settings.getDelta()) return;

        //Ключи всех фрагментов данной темы
        List<String> keys = new LinkedList<>(fragments.keySet(theme));
        if (keys.size() <= Settings.getMinimalFragmentsPerTheme()) return;

        //Выделение ключей схожих фрагментов по "листам веток"
        List<List<String>> similarFragments = new LinkedList<>();
        for (String key : keys) {

            int branch = similarFragments.size();
            checkBranches:
            for (int j = 0; j < similarFragments.size(); j++) {
                for (String similarFragmentKey : similarFragments.get(j)) {

                    if (Shingle.compare(fragments.getContent(key), fragments.getContent(similarFragmentKey))) {
                        branch = j;
                        break checkBranches;
                    }
                }
            }

            List<String> branchKeys = new LinkedList<>();
            branchKeys.add(key);
            if (branch < similarFragments.size()) {
                branchKeys.addAll(similarFragments.get(branch));
                similarFragments.set(branch, branchKeys);
            } else {
                similarFragments.add(branchKeys);
            }
        }

        //Сортировка на первичность к удалению
        Comparator<String> fragmentKeysSort = (k1, k2) -> {
            int countFirst = countKeyWords(fragments.get(k1).content(), theme.getKeyWords());
            int countSecond = countKeyWords(fragments.get(k2).content(), theme.getKeyWords());
            return countFirst - countSecond;
        };

        //Сортировка по уменьшению веток
        similarFragments.sort(Comparator.comparingInt(value -> -value.size()));
        //Удаление лишних фрагментов по веткам в теме
        deleteOnBranch:
        for (List<String> similarBranchKeys : similarFragments) {
            int branchSize = similarBranchKeys.size();
            if (branchSize <= 1) break;

            //Сортировка что первично на удаление на этой ветке: сначала доп литра потом по увеличению ключевых слов
            similarBranchKeys.sort(fragmentKeysSort);
            for (String branchKey : similarBranchKeys) {
                if (percents <= theme.getPercent()) break deleteOnBranch;
                if (branchSize <= 1) break;

                int deltaWords = fragments.get(branchKey).countWords();
                double newPercents = (fragments.countWords(theme) - deltaWords) / wordsPerPercent;
                //Удаление если при нем отклонение уменьшается
                if (Math.abs(percents - theme.getPercent()) > Math.abs(newPercents - theme.getPercent())) {
                    totalWords -= deltaWords;
                    wordsPerPercent = Math.min(Settings.getMaxWords() / 100, totalWords / 100);
                    fragments.remove(branchKey);
                    keys.remove(branchKey);

                    percents = newPercents;
                    branchSize--;
                }
            }
        }

        //Удалеие оставшихся
        keys.sort(fragmentKeysSort);
        int size = keys.size();
        double themePercent = theme.getPercent();
        for (String key : keys) {
            if (percents <= theme.getPercent() || size <= Settings.getMinimalFragmentsPerTheme()) break;

            int deltaWords = fragments.get(key).countWords();
            double newPercents = (fragments.countWords(theme) - deltaWords) / wordsPerPercent;
            if (Math.abs(percents - themePercent) > Math.abs(newPercents - themePercent) && Math.abs(newPercents) > themePercent - Settings.getDelta()) {
                totalWords -= deltaWords;
                wordsPerPercent = Math.min(Settings.getMaxWords() / 100, totalWords / 100);
                fragments.remove(key);

                percents = newPercents;
                size--;
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
        int max = 4;

        //Получение темы с наибольшим количеством совпадением ключевых слов
        Theme resultTheme = null;
        for (Theme theme : themes) {

            int matches = countKeyWords(content, theme.getKeyWords());

            if (matches > max) {
                max = matches;
                resultTheme = theme;
            }
        }

        return resultTheme;
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


    /**
     * ? Нет учета N-грамм
     * ? Добавление синонимов
     * Получение количества ключевых слов в тексте
     *
     * @param text     Проверяемый текст
     * @param keywords Ключевые слова
     * @return Количество совпадений
     */
    private static int countKeyWords(String text, Set<String> keywords) {
        List<String> words = new LinkedList<>();
        Set<String> stopWords = stopWords();

        Arrays.stream(text.split("[\\pP\\s]"))
                .filter(element -> !element.isEmpty())
                .map(word -> word.toLowerCase(Locale.ROOT).trim())
                .filter(word -> !stopWords.contains(word))
                .forEach(word -> words.addAll(Lucene.getNormalForms(word)));

        words.removeIf(word -> !keywords.contains(word));
        return words.size();
    }
}
