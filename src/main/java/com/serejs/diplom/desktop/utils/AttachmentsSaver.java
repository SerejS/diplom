package com.serejs.diplom.desktop.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class AttachmentsSaver {
    public static void saveBinaryImage(String bin, String name, String folder, File dir) throws IOException {
        byte[] imageByte = Base64.getDecoder().decode(bin);
        var newFile = new File(dir.getPath() + "/" + folder + "/" + name);
        FileUtils.writeByteArrayToFile(newFile, imageByte);
    }
}
