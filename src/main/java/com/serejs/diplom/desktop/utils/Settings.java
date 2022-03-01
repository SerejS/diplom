package com.serejs.diplom.desktop.utils;

public class Settings {
    //Настройки анализа
    private static final short minimalFragmentsPerTheme = 1;
    private static final short delta = 3;
    private static final int maxWords = 5000;
    private static final long minimalWords = 50L;
    private static final long minKeyWords = 2L;

    private static final float minConcentration = 0.01f;
    private static final long maxMicroRange = 3;


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

    public static float getMinConcentration() {return minConcentration;}

    public static long getMaxMicroRange() {return maxMicroRange;}
}
