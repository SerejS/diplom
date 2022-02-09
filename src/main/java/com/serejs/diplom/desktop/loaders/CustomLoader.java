package com.serejs.diplom.desktop.loaders;


import com.serejs.diplom.desktop.text.container.Format;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class CustomLoader implements ContentLoader {
    private final Format format;

    public CustomLoader(Format format) {
        this.format = format;
    }

    /**
     * Функция получение глав книги из файла пользовательского формата
     * @param uri Файл книги
     * @return Получение глав книги
     */
    @Override
    public Map<String, String> load(URI uri) throws Exception {

        //Получение текста из файла
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(uri.getPath()))) {
            reader.lines().forEach(line -> text.append(line).append('\n'));
        } catch (IOException e) {
            System.err.println("Ошибка получения текста из файла: " + uri);
        }

        Map<String, String> fragments = new HashMap<>();

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

        return fragments;
    }
}
