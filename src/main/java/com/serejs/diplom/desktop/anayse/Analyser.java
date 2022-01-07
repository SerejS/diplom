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
    public static void analise(Set<Literature> literatures, List<Theme> allThemes) {
        //Помещение всех текствоых фрагментов в одну мапу (title, content)
        HashMap<String, String> allFragments = new HashMap<>();
        literatures.forEach(literature -> allFragments.putAll(literature.getFragments()));

        //Определение тем для фрагментов (title, theme)
        HashMap<String, Theme> fragmentTheme = new HashMap<>();

        for (String title : allFragments.keySet()) {
            Theme theme = null;
            while (true) {
                List<Theme> subThemes = subThemes(theme, allThemes);
                if (subThemes.isEmpty()) break;
                theme = locate(allFragments.get(title), subThemes);
            }

            fragmentTheme.put(title, theme);
        }


    }

    private static Theme locate(String content, List<Theme> themes) {
        List<String> contentWords = Stream
                .of(content.replaceAll("\\p{Punct}", "").toLowerCase(Locale.ROOT).split(" "))
                .filter(el -> el.length() > minWordLength)
                .collect(Collectors.toCollection(LinkedList::new));

        int max = 0;
        Theme resultTheme = null;
        for (Theme theme : themes) {
            Set<String> keyWords = theme.getKeyWords();
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
    private static List<Theme> subThemes(Theme root, List<Theme> themes) {
        return themes.stream().filter(theme -> root == theme).collect(Collectors.toList());
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
