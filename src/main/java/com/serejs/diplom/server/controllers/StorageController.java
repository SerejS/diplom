package com.serejs.diplom.server.controllers;

import com.serejs.diplom.server.repositories.LiteratureRepository;
import com.serejs.diplom.server.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/api/files")
public class StorageController {
    private final AmazonS3Service s3Service;
    private final LiteratureRepository repository;

    @Autowired
    public StorageController(
            @Qualifier("amazonS3ServiceImpl") AmazonS3Service s3Service,
            LiteratureRepository repository
    ) {
        this.s3Service = s3Service;
        this.repository = repository;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> upload(
            @RequestParam Long literatureId,
            @RequestPart(value = "file") MultipartFile file) throws Exception {

        var literature = repository.getById(literatureId);
        var fileName = file.getOriginalFilename();

        s3Service.uploadFile(fileName, file.getBytes());
        literature.setPath(fileName);

        return new ResponseEntity<>(fileName, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ByteArrayResource> download(@RequestParam Long literatureId) throws Exception {
        var literature = repository.getById(literatureId);

        var key = literature.getPath();
        var fileBytes = s3Service.downloadFile(key);
        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        return ResponseEntity
                .ok()
                .contentLength(fileBytes.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + key + "\"")
                .body(resource);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long literatureId) throws Exception {
        var lit = repository.getById(literatureId);
        var key = lit.getPath();

        repository.delete(lit);
        s3Service.deleteFile(key);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
