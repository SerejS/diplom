package com.serejs.diplom.desktop.loaders;


import com.serejs.diplom.desktop.text.container.FormatLiterature;
import com.serejs.diplom.desktop.text.container.Literature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CustomLoader extends AbstractLoader {

    /**
     * Функция получение содержания книги из файла пользовательского формата
     *
     * @param literature Источник
     */
    @Override
    public void load(Literature literature) {
        if (!(literature instanceof FormatLiterature formatSource)) return;

        var uri = formatSource.getUri();
        var format = formatSource.getFormat();

        //Получение текста из файла
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(uri.getPath()))) {
            reader.lines().forEach(line -> text.append(line).append('\n'));
        } catch (IOException e) {
            System.err.println("Ошибка получения текста из файла: " + uri);
        }

        //Разделение текста на главы (разделитель до названия главы)
        for (String p : text.toString().split(format.getPrev())) {
            //Если глава не пустая
            if (p.length() != 0) {
                //Получение символа между названием главы и содержанием
                int mid = p.indexOf(format.getMid());

                //Добавление названия главы и содержания в мапу
                fragments.put(
                        p.substring(0, mid),
                        p.substring(mid + 1).split(format.getAfter())[0]
                );
            }
        }
    }
}
