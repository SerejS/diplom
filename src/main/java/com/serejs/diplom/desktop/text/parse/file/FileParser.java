package com.serejs.diplom.desktop.text.parse.file;

import java.io.*;
import java.util.Objects;

public class FileParser {
    public static String getText(String uri) {
        StringBuilder text = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(Objects.requireNonNull(FileParser.class.getClassLoader().getResource(uri)).getFile())
        )) {
            reader.lines().forEach(text::append);
        } catch (IOException e) {
            System.err.println("Ошибка получения текста из файла: " + uri);
        }

        return text.toString();
    }
}
