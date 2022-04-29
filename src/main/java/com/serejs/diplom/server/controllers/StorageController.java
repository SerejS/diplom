package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/files")
public class StorageController {
    private final AmazonS3Service s3Service;

    @Autowired
    public StorageController(@Qualifier("amazonS3ServiceImpl") AmazonS3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping(value = "/{bucketName}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> upload(
            @PathVariable("bucketName") String bucketName,
            @RequestPart(value = "file") MultipartFile files) throws Exception {
        System.out.println(bucketName);
        s3Service.uploadFile(bucketName, files.getOriginalFilename(), files.getBytes());
        Map<String, String> result = new HashMap<>();
        result.put("key", files.getOriginalFilename());
        return result;
    }
}
