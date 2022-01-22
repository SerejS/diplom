package com.serejs.diplom.desktop.anayse;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;


/**
 * Тестовый шингл
 */
public class Shingle {
    private static final int SHINGLE_LEN = 2;

    /**
     * Метод занимается неполной канонизацией строки. Вырезает из строки стоп-символы и слова
     *
     * @param str строка, для канонизации
     * @return канонизированная строка
     */
    private static List<String> canonize(String str) throws IOException {
        HashSet<String> stopWords = Analyser.stopWords();

        LuceneMorphology morph = new RussianLuceneMorphology();
        return Arrays.stream(str.toLowerCase(Locale.ROOT).replaceAll("\\p{Punct}", "").split("\\s"))
                .map(String::trim)
                .filter(element -> !element.isEmpty())
                .filter(word -> !stopWords.contains(word))
                .map(word -> morph.getNormalForms(word).get(0))
                .toList();
    }

    /**
     * Метод разбивает текст на шинглы, а затем вычисляет их контрольные суммы.
     *
     * @param strNew строка, для создания шинглов
     * @return ArrayList шинглов в числовом виде
     */
    public static ArrayList<Integer> genShingle(String strNew) throws IOException {
        ArrayList<Integer> shingles = new ArrayList<>();
        List<String> words = canonize(strNew);
        int shinglesNumber = words.size() - SHINGLE_LEN;

        //Create all shingles
        for (int i = 0; i <= shinglesNumber; i++) {
            StringBuilder shingle = new StringBuilder();

            //Create one shingle
            for (int j = 0; j < SHINGLE_LEN; j++) {
                shingle.append(words.get(i + j)).append(" ");
            }

            shingles.add(shingle.toString().hashCode());
        }

        return shingles;
    }

    /**
     * Метод сравнивает две последовательности шинглов
     *
     * @param text1 первый текст
     * @param text2 второй текст
     * @return процент сходства шинглов
     */
    public static boolean compare(String text1, String text2) throws IOException {
        ArrayList<Integer> textShingles1New = genShingle(text1);
        ArrayList<Integer> textShingles2New = genShingle(text2);

        int textShingles1Number = textShingles1New.size();
        int textShingles2Number = textShingles2New.size();

        double similarShinglesNumber = 0;

        for (Integer firstSingle : textShingles1New) {
            for (Integer secondShingle : textShingles2New) {
                if (firstSingle.equals(secondShingle)) similarShinglesNumber++;
            }
        }

        System.out.println((similarShinglesNumber / ((textShingles1Number + textShingles2Number) / 2.0)) * 100);
        return ((similarShinglesNumber / ((textShingles1Number + textShingles2Number) / 2.0)) * 100) > 0.6;
    }


}