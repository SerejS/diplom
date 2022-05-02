package com.serejs.diplom.server.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class AmazonS3ServiceImpl implements AmazonS3Service {
    private final String bucketName = "litanalis";
    private final AmazonS3 s3;

    public AmazonS3ServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public void uploadFile(String originalFilename, byte[] bytes) throws Exception {

        File file = upload(originalFilename, bytes);
        s3.putObject(bucketName, originalFilename, file);

    }

    @Override
    public byte[] downloadFile(String fileUrl) {
        return getFile(fileUrl);
    }

    @Override
    public void deleteFile(String fileUrl) {
        s3.deleteObject(bucketName, fileUrl);
    }

    @Override
    public List<String> listFiles() {
        List<String> list = new LinkedList<>();
        s3.listObjects(bucketName).getObjectSummaries().forEach(itemResult -> {
            list.add(itemResult.getKey());
            System.out.println(itemResult.getKey());
        });
        return list;
    }

    @Override
    public File upload(String name, byte[] content) throws Exception {
        File file = new File("/" + name);

        try (FileOutputStream iofs = new FileOutputStream(file)) {
            iofs.write(content);
        }

        return file;
    }

    @Override
    public byte[] getFile(String key) {
        S3Object obj = s3.getObject(bucketName, key);
        S3ObjectInputStream stream = obj.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
