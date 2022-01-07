package com.serejs.diplom.desktop.anayse;

import com.serejs.diplom.desktop.App;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.Theme;

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


    /**
     * Функция выборта темы для текста из предложенных
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
                    .mapToInt(entrySet -> entrySet.getValue().percent())
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
    private static int countWords(HashMap<String, Theme> fragmentTheme, HashMap<String, String> fragments, Theme theme) {
        return fragmentTheme.entrySet().stream()
                .filter(entry -> entry.getValue() == theme)
                .mapToInt(entry -> fragments.get(entry.getKey()).split(" ").length)
                .reduce(Integer::sum).getAsInt();
    }


    /**
     * Функция получения из файла стоп-слов
     * @return Множество слов
     */
    private static HashSet<String> stopWords() {
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
}
