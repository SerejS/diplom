package com.serejs.diplom.desktop.utils;

public class Settings {
    //Настройки анализа
    private static final short minimalFragmentsPerTheme = 1;
    private static final short delta = 5;
    private static final int maxWords = 50000;


    public static short getMinimalFragmentsPerTheme() {
        return minimalFragmentsPerTheme;
    }

    public static short getDelta() {
        return delta;
    }

    public static int getMaxWords() {
        return maxWords;
    }

}
