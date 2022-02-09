package com.serejs.diplom.desktop.loaders;

import java.net.URI;
import java.util.Map;

public interface ContentLoader {
    Map<String, String> load(URI uri) throws Exception;
}
