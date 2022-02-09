package com.serejs.diplom.desktop.loaders;

import com.serejs.diplom.desktop.text.container.Fragment;

import java.net.URI;
import java.util.List;
import java.util.Map;

public interface ContentLoader {
    Map<String, String> load(URI uri) throws Exception;
}
