package com.serejs.diplom.desktop.utils;

import lombok.Getter;
import lombok.Setter;

public class Settings {
    //Настройки анализа
    @Getter @Setter
    private static short minimalFragmentsPerTheme = 1;
    @Getter @Setter
    private static short delta = 3;

    @Getter @Setter
    private static int maxLengthTitle = 80;

    @Getter @Setter
    private static long minWords = 50L;
    @Getter @Setter
    private static long maxWords = 5000L;

    @Getter @Setter
    private static long minKeyNGrams = 2L;

    @Getter @Setter
    private static float minConcentration = 0.01f;
    @Getter @Setter
    private static long maxMicroRange = 3;
    @Getter @Setter
    private static boolean autoExtracting = true;
    @Getter @Setter
    private static boolean saveAttachments = false;

    @Getter @Setter
    private static String mainCX;
    @Getter @Setter
    private static String mainKey;
    @Getter @Setter
    private static String subCX;
    @Getter @Setter
    private static String subKey;
}
