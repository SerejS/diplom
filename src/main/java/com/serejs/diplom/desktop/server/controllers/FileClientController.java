package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Source;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;

import java.io.File;

public class FileClientController extends AbstractClientController {
    private static final String endpoint = "/api/files";

    public static void request(Source source) throws Exception {
        if (source.getSourceType() == SourceType.WEB) throw new Exception("Данный тип источника не имеет файла");

        var encoding = getEncoding();

        HttpClient httpclient = HttpClients.createDefault();

        var file = new File(source.getUri());
        var fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);
        var request = new HttpPost(baseUrl + endpoint);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        request.setEntity(builder.build());

        HttpResponse response = httpclient.execute(request);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED)
            throw new Exception("Ошибка отправления файла на сервер");

        SourceClientController.createSource(source);
    }
}
