package com.serejs.diplom.desktop.utils;

public class Settings {
    //Настройки анализа
    private static final short minimalFragmentsPerTheme = 1;
    private static final short delta = 3;
    private static final int maxWords = 50000;
    private static final long minimalWords = 4L;


    public static short getMinimalFragmentsPerTheme() {
        return minimalFragmentsPerTheme;
    }

    public static short getDelta() {
        return delta;
    }

    public static int getMaxWords() {
        return maxWords;
    }

    public static long getMinimalWords() {return minimalWords;}
}
