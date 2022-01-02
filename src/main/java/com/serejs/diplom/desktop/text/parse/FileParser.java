package com.serejs.diplom.desktop.text.parse;

import java.io.*;

public class FileParser {
    public static String getText(File file) {
        StringBuilder text = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines().forEach(line -> text.append(line).append('\n'));
        } catch (IOException e) {
            System.err.println("Ошибка получения текста из файла: " + file.getName());
        }

        return text.toString();
    }
}
