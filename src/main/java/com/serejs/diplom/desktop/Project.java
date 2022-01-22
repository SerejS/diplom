package com.serejs.diplom.desktop;

import com.serejs.diplom.desktop.anayse.Analyser;
import com.serejs.diplom.desktop.text.Converter;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.text.parse.WebParser;

import java.util.List;
import java.util.Set;

/**
 * Проект
 * Получает литературу и возращает основные положения рассматриваемых тем
 */
public class Project {
    Set<Literature> literature;
    List<Theme> themes;

    /**
     * Конструктор для создания нового проекта
     *
     * @param sources    источники, которые хранятся на компьюетере
     * @param themes     рассматриваемые темы
     * @param defaultWeb основная литература из интернет-источников
     * @param extendWeb  дополнительная литература из интернет источников
     */
    Project(
            List<Source> sources,
            List<Theme> themes,
            WebParser defaultWeb,
            WebParser extendWeb
    ) throws Exception {
        this.themes = themes;

        literature = Converter.convert(sources);
        if (defaultWeb != null) literature.addAll(defaultWeb.literatureFromWeb(themes));
        if (extendWeb != null) literature.addAll(extendWeb.literatureFromWeb(themes));
    }

    /**
     * Метод в котором запускается анализ и все остальное (синхронизация, залив на сервер и тд)
     */
    public void execute() {
        Analyser.analise(literature, themes);
    }
}
