package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.enums.AttachmentType;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;

public record Attachment(String name, Object content, AttachmentType type) {
    public void save(String folder, File dir) throws IOException {
        var newFile = new File(dir.getPath() + "/" + folder + "/" + name);

        switch (type) {
            case TABLE -> {
                if (!(content instanceof String tab)) return;

                //Дозаписывание в файл с таблицами
                FileUtils.writeStringToFile(newFile, tab, "UTF-8", true);

            }
            case IMAGE -> {
                if (!(content instanceof String content)) return;

                byte[] imageBytes;
                if (content.contains("http")) {
                    URL url = new URL(content);

                    try (InputStream is = url.openStream()) {
                        imageBytes = is.readAllBytes();
                    } catch (Exception e) {
                        return;
                    }
                } else {
                    imageBytes = Base64.getDecoder().decode(content);
                }

                FileUtils.writeByteArrayToFile(newFile, imageBytes);
            }
            case AUDIO -> {
            }
        }
    }
}
