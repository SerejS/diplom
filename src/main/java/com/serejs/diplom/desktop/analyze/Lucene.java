package com.serejs.diplom.desktop.analyze;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.WrongCharaterException;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Lucene {
    private static LuceneMorphology morph;

    private static LuceneMorphology getMorph() throws IOException {
        if (morph == null) morph = new RussianLuceneMorphology();
        return morph;
    }

    public static List<String> getNormalForms(String word) {
        List<String> normalForms = new LinkedList<>();

        try {
            normalForms.addAll(getMorph().getNormalForms(word));
        } catch (WrongCharaterException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        return normalForms;
    }
}
