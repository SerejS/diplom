package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.text.parse.file.LiteratureType;

public record Source(String url, LiteratureType type, Format format, boolean main) {}
