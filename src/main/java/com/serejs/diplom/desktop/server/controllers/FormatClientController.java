package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Format;
import org.apache.http.HttpException;

import java.io.IOException;
import java.net.URISyntaxException;

public class FormatClientController extends AbstractClientController {
    private static final String endpoint = "/api/format";

    //Отправление поисковых движков на сервер
    public static void sendFormat(Format format) throws HttpException, IOException, URISyntaxException {
        var response = postRequest(endpoint, format);
        format.setId(Long.parseLong(response));
    }

    public static void deleteFormat(Long formatId) throws HttpException, IOException, URISyntaxException {
        deleteRequest(endpoint + formatId);
    }
}
