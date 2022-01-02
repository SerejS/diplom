package com.serejs.diplom.desktop.text.container;

import java.util.HashMap;
import java.util.Map;

public class Literature {
    private final static int minLenFragment = 150;
    private Map<String, String> fragments = new HashMap<>();
    private boolean main = true;

    public Literature() {}

    public Literature(Map<String, String> fragments) {
        setFragments(fragments);
    }

    public Literature(Map<String, String> fragments, boolean main) {
        this.main = main;
        setFragments(fragments);
    }

    private void setFragments(Map<String, String> fragments) {
        fragments.keySet().forEach(key -> {
            if (fragments.get(key).length() >= minLenFragment) {
                this.fragments.put(key, fragments.get(key));
            }
        });
    }

    public Map<String, String> getFragments() {
        return fragments;
    }

    public boolean isMain() {
        return main;
    }
}
