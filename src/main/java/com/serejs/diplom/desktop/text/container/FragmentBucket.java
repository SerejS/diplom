package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.anayse.Analyser;

import java.util.*;
import java.util.stream.Collectors;

public class FragmentBucket {
    private final HashMap<String, Fragment> fragments;

    public FragmentBucket(Set<Literature> literatures, List<Theme> themes) {
        fragments = new HashMap<>();

        for (Literature literature : literatures) {
            literature.getFragments().forEach((key, content) -> {
                        Theme theme = Analyser.getTheme(content, Theme.getLeafThemes(themes));
                        if (theme == null) return;
                        if (content.split("\\s").length < 500) return;

                        fragments.put(key, new Fragment(content, theme, literature.isMain()));
                    }
            );
        }

        recalculateThemes();
    }



    public void remove(String key) {
        fragments.remove(key);
    }


    /**
     * Подсчет количества слов в фрагментах данной темы
     *
     * @param theme Тема, по которой ищется
     * @return Количество слов
     */
    public int countWords(Theme theme) {
        return this.fragments.values().stream()
                .filter(fragment -> fragment.theme().equals(theme))
                .mapToInt(Fragment::countWords)
                .sum();
    }


    /**
     * Функция перерасчета процентного содержания выбранных тем
     */
    private void recalculateThemes() throws IllegalArgumentException {
        Set<String> allKeys = new HashSet<>(fragments.keySet());
        while (!allKeys.isEmpty()) {
            Set<String> keys = new HashSet<>();
            Set<Theme> themes = getThemes();
            themes.forEach(t -> keys.addAll(keySet(t)));
            allKeys.removeAll(keys);

            double sum = themes.stream().mapToDouble(Theme::getPercent).sum();
            if (sum == 100) continue;

            themes.forEach(t -> t.setPercent(t.getPercent() * 100 / sum));
        }
    }


    public Map<String, Fragment> get() {
        return fragments;
    }

    public Set<Theme> getThemes() {
        return fragments.values().stream().map(Fragment::theme).collect(Collectors.toSet());
    }

    /**
     * Функция получения из всего списка тем дочерние принимаемой
     *
     * @param root Родитель искомых тем
     * @return Дочерние темы
     */
    public Set<Theme> getSubThemes(Theme root) {
        return this.fragments.values()
                .stream().map(Fragment::theme)
                .filter(theme -> root == theme.getRoot())
                .collect(Collectors.toSet());
    }


    public Set<String> keySet() {
        return this.fragments.keySet();
    }

    public Set<String> keySet(Theme theme) {
        return keySet().stream()
                .filter(key -> fragments.get(key).theme() == null || fragments.get(key).theme().equals(theme))
                .collect(Collectors.toSet());
    }

    public Set<Fragment> getAllFragments() {
        return new HashSet<>(fragments.values());
    }

    public Fragment get(String key) {
        return fragments.get(key);
    }

    public String getContent(String key) {
        return this.fragments.get(key).content();
    }
}
