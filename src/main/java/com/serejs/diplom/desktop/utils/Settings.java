package com.serejs.diplom.desktop.utils;

public class Settings {
    //Настройки анализа
    private static final short minimalFragmentsPerTheme = 1;
    private static final short delta = 3;

    private static final long minWords = 50L;
    private static final long maxWords = 5000L;

    private static final long minKeyWords = 2L;

    private static final float minConcentration = 0.01f;
    private static final long maxMicroRange = 3;
    private static boolean autoReferring = false;


    public static short getMinimalFragmentsPerTheme() {
        return minimalFragmentsPerTheme;
    }

    public static short getDelta() {
        return delta;
    }

    public static long getMaxWords() {
        return maxWords;
    }

    public static long getMinWords() {return minWords;}

    public static float getMinConcentration() {return minConcentration;}

    public static long getMaxMicroRange() {return maxMicroRange;}
}
