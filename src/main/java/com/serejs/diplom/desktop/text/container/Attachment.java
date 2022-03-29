package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.enums.AttachmentType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public record Attachment(String name, Object content, AttachmentType type) {
    public void save(String folder, File dir) throws IOException {
        if (!(content instanceof String str)) return;

        var newFile = new File(dir.getPath() + "/" + folder + "/" + name);

        switch (type) {
            //Дозаписывание в файл с таблицами
            case TABLE -> FileUtils.writeStringToFile(newFile, str, "UTF-8", true);
            case IMAGE -> {
                byte[] imageBytes;
                if (str.contains("http")) {
                    URL url = new URL(str);

                    try (InputStream is = url.openStream()) {
                        imageBytes = is.readAllBytes();
                    } catch (Exception e) {
                        return;
                    }
                } else {
                    imageBytes = Base64.getDecoder().decode(str);
                }

                FileUtils.writeByteArrayToFile(newFile, imageBytes);
            }
            case AUDIO -> {
                //IF EPUB than bytes
                //Форматы аудио
                //ogg/vorbis
                //wav
                //mp3
                //AAC

                var url = new URL(str);
                try (var is = url.openStream()) {
                    byte[] bytes = is.readAllBytes();
                    FileUtils.writeByteArrayToFile(newFile, bytes);
                }
            }
        }
    }
}
