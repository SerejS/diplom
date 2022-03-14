package com.serejs.diplom.desktop.utils;

public class Settings {
    //Настройки анализа
    private static short minimalFragmentsPerTheme = 1;
    private static short delta = 3;

    private static long minWords = 50L;
    private static long maxWords = 5000L;

    private static long minKeyWords = 2L;

    private static float minConcentration = 0.01f;
    private static long maxMicroRange = 3;
    private static boolean autoReferring = true;

    private static String mainCX;
    private static String mainKey;
    private static String subCX;
    private static String subKey;

    public static String getMainCX() {
        return mainCX;
    }

    public static void setMainCX(String mainCX) {
        Settings.mainCX = mainCX;
    }

    public static String getMainKey() {
        return mainKey;
    }

    public static void setMainKey(String mainKey) {
        Settings.mainKey = mainKey;
    }

    public static String getSubCX() {
        return subCX;
    }

    public static void setSubCX(String subCX) {
        Settings.subCX = subCX;
    }

    public static String getSubKey() {
        return subKey;
    }

    public static void setSubKey(String subKeys) {
        Settings.subKey = subKeys;
    }

    public static short getMinimalFragmentsPerTheme() {
        return minimalFragmentsPerTheme;
    }

    public static short getDelta() {
        return delta;
    }

    public static long getMaxWords() {
        return maxWords;
    }

    public static long getMinWords() {
        return minWords;
    }

    public static float getMinConcentration() {
        return minConcentration;
    }

    public static long getMaxMicroRange() {
        return maxMicroRange;
    }

    public static long getMinKeyWords() {
        return minKeyWords;
    }

    public static void setMinKeyWords(long minKeyWords) {
        Settings.minKeyWords = minKeyWords;
    }


    public static boolean isAutoReferring() {
        return autoReferring;
    }

    public static void setAutoReferring(boolean autoReferring) {
        Settings.autoReferring = autoReferring;
    }

    public static void setMinimalFragmentsPerTheme(short minimalFragmentsPerTheme) {
        Settings.minimalFragmentsPerTheme = minimalFragmentsPerTheme;
    }

    public static void setDelta(short delta) {
        Settings.delta = delta;
    }

    public static void setMinWords(long minWords) {
        Settings.minWords = minWords;
    }

    public static void setMaxWords(long maxWords) {
        Settings.maxWords = maxWords;
    }

    public static void setMinConcentration(float minConcentration) {
        Settings.minConcentration = minConcentration;
    }

    public static void setMaxMicroRange(long maxMicroRange) {
        Settings.maxMicroRange = maxMicroRange;
    }
}
