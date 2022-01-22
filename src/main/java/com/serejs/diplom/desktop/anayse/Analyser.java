package com.serejs.diplom.desktop.anayse;

import com.serejs.diplom.desktop.App;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.Theme;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Analyser {
    private static final int minWordLength = 4;

    /**
     * Анализ всей литературы
     * @param literatures Анализируемая литература
     * @param allThemes Темы анализируемой литературы
     */
    public static void analise(Set<Literature> literatures, List<Theme> allThemes) {}


    public static void alignment(
            HashMap<String, Theme> fragmentTheme,
            HashMap<String, String> fragments,
            Integer maxWords
    ) throws Exception {
        short minimalFragmentsPerTheme = 1;
        short delta = 5;
        int totalCountWords = fragments.values().stream()
                .map(s -> s.split(" ").length)
                .reduce(Integer::sum).get();

        Theme largestTheme = fragmentTheme.values().stream().max(Comparator.comparingInt(Theme::percent)).get();

        int wordsPerPercent = maxWords * largestTheme.percent() / 100;

        Set<Theme> themes = new HashSet<>(fragmentTheme.values());

        for (Theme theme : themes) {
            //Реальное процентное содержание текстов данной темы
            int totalWords = Analyser.countWords(fragmentTheme, fragments, theme);
            int percents = totalWords / wordsPerPercent;
            if (percents < theme.percent() + delta) continue;

            //Ключи всех фрагментов данной темы
            List<String> keys = fragmentTheme.keySet().stream().filter(key -> fragmentTheme.get(key) == theme).toList();
            if (keys.size() <= minimalFragmentsPerTheme) continue;

            //Выделение ключей схожих фрагментов по "листам веток"
            List<List<String>> similarFragments = new LinkedList<>();
            for (String key : keys) {

                int branch = similarFragments.size();
                checkBranches:
                for (int j = 0; j < similarFragments.size(); j++) {
                    for (String similarFragmentKey : similarFragments.get(j)) {

                        if (Shingle.compare(fragments.get(key), fragments.get(similarFragmentKey))) {
                            branch = j;
                            break checkBranches;
                        }
                    }
                }

                List<String> branchKeys = new LinkedList<>();
                if (branch < similarFragments.size()) branchKeys = similarFragments.get(branch);
                branchKeys.add(key);

                similarFragments.add(branch, branchKeys);
            }

            similarFragments.sort(Comparator.comparingInt(value -> -value.size()));
            for (List<String> similarBranchKeys : similarFragments) {
                if (similarBranchKeys.size() == 1) break;

                for (String branchKey : similarBranchKeys) {
                    int newPercents = percents - fragments.get(branchKey).split(" ").length / wordsPerPercent;

                    if (Math.abs(percents - theme.percent()) > Math.abs(newPercents - theme.percent())) {
                        percents = newPercents;

                        fragmentTheme.remove(branchKey);
                        fragments.remove(branchKey);
                        keys.remove(branchKey);
                    }
                }
            }
        }

    }


    /**
     * Функция перерасчета процентного содержания выбранных тем
     * @param fragmentTheme Мапа названий фрагментов против выбранной темы
     */
    private static void recalculateThemes(HashMap<String, Theme> fragmentTheme) {
        Set<String> allKeys = new HashSet<>(fragmentTheme.keySet());
        while (!allKeys.isEmpty()) {
            Set<String> iterateKeys = new HashSet<>(allKeys);
            String key = iterateKeys.stream().findFirst().orElseThrow();

            Set<String> keys = allKeys
                    .stream().filter(k -> fragmentTheme.get(k).root() == fragmentTheme.get(key).root())
                    .collect(Collectors.toSet());
            allKeys.removeAll(keys);

            int sum = fragmentTheme.entrySet().stream()
                    .filter(entrySet -> keys.contains(entrySet.getKey()))
                    .map(Map.Entry::getValue)
                    .distinct()
                    .mapToInt(Theme::percent)
                    .sum();

            if (sum >= 100) {
                if (sum != 100) System.err.println("Сумма частей темы больше 100%");
                break;
            }


            keys.forEach(k -> {
                Theme t = fragmentTheme.get(k);
                fragmentTheme.put(k, t.recalculate((byte) (100 * t.percent() / sum)));
            });
        }
    }


    /**
     * Функция выбора темы для текста из предложенных
     * @param content Определяемый текст
     * @param themes Все преложенные темы
     * @return Выбранная тема
     */
    private static Theme getTheme(String content, List<Theme> themes) {
        List<String> contentWords = Stream
                .of(content.replaceAll("\\p{Punct}", "").toLowerCase(Locale.ROOT).split(" "))
                .filter(el -> el.length() > minWordLength)
                .collect(Collectors.toCollection(LinkedList::new));

        int max = 0;
        Theme resultTheme = null;
        for (Theme theme : themes) {
            Set<String> keyWords = theme.keyWords();
            HashSet<String> stopWords = stopWords();
            stopWords.removeAll(keyWords);

            List<String> significantWords = new LinkedList<>(contentWords);
            significantWords.removeAll(stopWords);
            significantWords.retainAll(keyWords);

            int matches = significantWords.size();
            if (matches > max) {
                max = matches;
                resultTheme = theme;
            }
        }

        return resultTheme;
    }


    /**
     * Функция получения из всего списка тем дочерние принимаемой
     * @param root Родитель искомых тем
     * @param themes Весь список тем
     * @return Дочерние темы
     */
    private static List<Theme> subThemes(Theme root, Collection<Theme> themes) {
        return themes.stream().filter(theme -> root == theme).collect(Collectors.toList());
     }


    /**
     * Подсчет количества слов в фрагментах данной темы
     * @param fragmentTheme (title, theme)
     * @param fragments (title, content)
     * @param theme Тема, по которой ищется
     * @return Количество слов
     */
    public static int countWords(HashMap<String, Theme> fragmentTheme, HashMap<String, String> fragments, Theme theme) {
        return fragmentTheme.entrySet().stream()
                .filter(entry -> entry.getValue() == theme)
                .mapToInt(entry -> fragments.get(entry.getKey()).split(" ").length)
                .reduce(Integer::sum).getAsInt();
    }


    /**
     * Функция получения из файла стоп-слов
     * @return Множество стоп-слов
     */
    public static HashSet<String> stopWords() {
        HashSet<String> stopWords = new HashSet<>();
        try(BufferedReader reader =
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
     * Получение колличества ключевых слов в тексте
     * @param text Проверяемый текст
     * @param keywords Ключевые слова
     * @return Количество совпадений
     * @throws IOException Получение экземпляра RuLuceneMorphology
     */
    private static int countKeyWords(String text, Set<String> keywords) throws IOException {
        List<String> words = new LinkedList<>();

        Set<String> stopWords = stopWords();
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();

        Arrays.stream(text.split("[\\pP\\s]"))
                .filter(element -> !element.isEmpty())
                .map(word -> word.toLowerCase(Locale.ROOT).trim())
                .filter(word -> !stopWords.contains(word))
                .forEach(word -> words.addAll(luceneMorph.getNormalForms(word)));

        words.removeIf(word -> !keywords.contains(word));
        return words.size();
    }
}
