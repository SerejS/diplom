package com.serejs.diplom.desktop.analyze;

import com.serejs.diplom.desktop.text.container.Fragment;
import com.serejs.diplom.desktop.utils.Settings;

import java.util.Arrays;
import java.util.LinkedList;

public class AutoSummarizer {
    public static Fragment summarize(Fragment fragment) {
        //Получение абзацев текста
        var paragraphs = Arrays.stream(fragment.getContent().split("\n"));
        var microFragments = paragraphs
                .map(p -> new Fragment(p, fragment.getTheme(), fragment.getType())).toList();

        //Отбор индексов абзацев с минимальной конценрацией ключевых слов
        var indexes = new LinkedList<Integer>();
        for (int i = 0; i < microFragments.size(); i++) {
            if (microFragments.get(i).getConcentration() >= Settings.getMinConcentration()) {
                indexes.add(i);
            }
        }

        //Составление текста из выбранных абзацев
        var result = new StringBuilder();
        for (int i = 0; i < indexes.size(); i++) {
            //Количество оставшихся абзацев между выбранными
            var range = indexes.get(i + 1) - indexes.get(i);
            //Если разница между выбранными больше максимально допущеной, то внутренние игнорируются
            if (range > Settings.getMaxMicroRange()) range = 1;

            for (int j = 0; j < range; j++) {
                result.append(microFragments.get(indexes.get(i) + j)).append('\n');
            }
        }

        return new Fragment(result.toString(), fragment.getTheme(), fragment.getType());
    }
}
