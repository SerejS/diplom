package com.serejs.diplom.desktop.text.container;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FragmentMap extends HashMap<String, Fragment> {
    private Fragment summarize(Fragment fragment) {
        double minKeyWordsDensity = 0.05;
        int maxDistance = 5;
        boolean sentence = true;
        Pattern delimiter = Pattern.compile(sentence ? "\\P{Pe}" : "\\n");

        List<String> microFragments = List.of(delimiter.split(fragment.getContent()));

        StringBuilder temp = new StringBuilder();
        StringBuilder summary = new StringBuilder();
        for (int i = 0, k = 0; i < microFragments.size(); i++) {
            if (i - k <= maxDistance) temp.append(microFragments.get(i)).append(sentence ? ". " : "\n");
            else temp.setLength(0);

            //Пропуск неудолетворительного микрофрагмента
            if (Arrays.stream(microFragments.get(i).split("\\P{all} | \\s")).filter(el -> !el.isEmpty()).count() < minKeyWordsDensity)
                continue;

            k = i;
            summary.append(temp);
            temp.setLength(0);
        }


        return new Fragment(summary.toString(), fragment.getTheme(), fragment.getType());
    }


    public double percent(Theme theme) {
        return (double) length(theme) / length();
    }



    public long length() {
        return this.values().stream().mapToLong(Fragment::getLength).sum();
    }

    /**
     * Полученный объем по текстам
     *
     * @param theme Тема, по которой ищется
     * @return Количество слов
     */
    public long length(Theme theme) {
        return this.values().stream()
                .filter(fragment -> fragment.getTheme().equals(theme))
                .mapToLong(Fragment::getLength)
                .sum();
    }


    /**
     * Функция перерасчета процентного содержания выбранных тем
     */
    public void recalculateThemes() throws IllegalArgumentException {
        Set<String> allKeys = new HashSet<>(this.keySet());

        while (!allKeys.isEmpty()) {
            Set<String> keys = new HashSet<>();
            Set<Theme> themes = getThemes();
            themes.forEach(t -> keys.addAll(keySet(t)));
            allKeys.removeAll(keys);

            double sum = themes.stream().mapToDouble(Theme::getPercent).sum();
            if (sum == 100) continue;

            themes.forEach(t -> t.setPercent(t.getPercent() / sum));
        }
    }


    /**
     * @return Множество используемых тем
     */
    public Set<Theme> getThemes() {
        return this.values().stream().map(Fragment::getTheme).collect(Collectors.toSet());
    }

    /**
     * Функция получения из всего списка тем дочерние принимаемой
     *
     * @param root Родитель искомых тем
     * @return Дочерние темы
     */
    public Set<Theme> getSubThemes(Theme root) {
        return this.values()
                .stream().map(Fragment::getTheme)
                .filter(theme -> root == theme.getRoot())
                .collect(Collectors.toSet());
    }

    /**
     * @return Ключи фрагментов, у которых данная тема
     */
    public Set<String> keySet(Theme theme) {
        return this.keySet().stream()
                .filter(key -> this.get(key).getTheme() == null || this.get(key).getTheme().equals(theme))
                .collect(Collectors.toSet());
    }

    public String getContent(String key) {
        return this.get(key).getContent();
    }
}
