package com.serejs.diplom.desktop.anayse;

import com.serejs.diplom.desktop.App;
import com.serejs.diplom.desktop.text.container.Fragment;
import com.serejs.diplom.desktop.text.container.FragmentBucket;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.Theme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Analyser {
    //Настройки анализа
    private static final short minimalFragmentsPerTheme = 1;
    private static final short delta = 5;
    private static final int maxWords = 50000;


    /**
     * Анализ всей литературы
     *
     * @param literatures Анализируемая литература
     * @param allThemes   Темы анализируемой литературы
     */
    public static void analise(Set<Literature> literatures, List<Theme> allThemes) {
        FragmentBucket fragments = new FragmentBucket(literatures, allThemes);

        int totalCount = fragments.getAllFragments().stream().mapToInt(Fragment::countWords).sum();
        alignment(fragments);

        fragments.get().forEach((key, f) -> System.out.printf("%s = %s %2.2f\n", f.theme().getTitle(), key, (double) 100 * f.countWords() / totalCount));
        System.out.println("Количество полученых фрагметов: " + fragments.getAllFragments().size());

        int totalCount2 = fragments.getAllFragments().stream().mapToInt(Fragment::countWords).sum();
        fragments.getThemes().forEach(t -> System.out.println(t.getTitle() + " " + t.getPercent() + " " + ((double) fragments.countWords(t) / totalCount2)));
    }


    /**
     * Выравнивание найденного материала
     *
     * @param fragments Обрабатываемый материал
     */
    public static void alignment(FragmentBucket fragments) {
        //Максимальное количество слов в одном проценте
        Theme largestTheme = fragments.getThemes().stream().max(Comparator.comparingDouble(Theme::getPercent)).orElseThrow();

        List<Theme> themes = new ArrayList<>(fragments.getThemes());
        for (int i = 0; i <= themes.size(); i++) {
            int totalWords = fragments.getAllFragments().stream().mapToInt(Fragment::countWords).sum();
            double wordsPerPercent = Math.min((double) maxWords / 100, (double) totalWords / 100);

            //Выравнивание процентного содержания относительно каждой темы
            for (int k = 0; k <= i && k < themes.size(); k++) {
                Theme theme = themes.get(k);
                //Реальное процентное содержание текстов данной темы
                double percents = fragments.countWords(theme) / wordsPerPercent;
                if (percents < theme.getPercent() + delta) continue;

                //Ключи всех фрагментов данной темы
                List<String> keys = new LinkedList<>(fragments.keySet(theme));
                if (keys.size() <= minimalFragmentsPerTheme) continue;

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
                    Fragment f1 = fragments.get(k1);
                    Fragment f2 = fragments.get(k2);

                    if (f1.main() && !f2.main()) return 1;
                    if (!f1.main() && f2.main()) return -1;

                    return countKeyWords(f1.content(), theme.getKeyWords()) - countKeyWords(f2.content(), theme.getKeyWords());
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
                            wordsPerPercent = Math.min(maxWords / 100, totalWords / 100);
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
                    if (percents <= theme.getPercent() || size <= minimalFragmentsPerTheme) break;

                    int deltaWords = fragments.get(key).countWords();
                    double newPercents = (fragments.countWords(theme) - deltaWords) / wordsPerPercent;
                    if (Math.abs(percents - themePercent) > Math.abs(newPercents - themePercent) && Math.abs(newPercents) > themePercent - delta) {
                        totalWords -= deltaWords;
                        wordsPerPercent = Math.min(maxWords / 100, totalWords / 100);
                        fragments.remove(key);

                        percents = newPercents;
                        size--;
                    }
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
                             Objects.requireNonNull(App.class.getClassLoader().getResource("stop_ru.txt")).getFile()
                     ))
        ) {
            stopWords = reader.lines().collect(Collectors.toCollection(HashSet::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stopWords;
    }


    /**
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
