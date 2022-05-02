package com.serejs.diplom.server.services;

import java.io.File;
import java.util.List;

public interface AmazonS3Service {
    void uploadFile(String originalFilename, byte[] bytes) throws Exception;

    byte[] downloadFile(String fileUrl) throws Exception;

    void deleteFile(String fileUrl) throws Exception;

    List<String> listFiles() throws Exception;

    File upload(String name, byte[] content) throws Exception;

    byte[] getFile(String key) throws Exception;
}
