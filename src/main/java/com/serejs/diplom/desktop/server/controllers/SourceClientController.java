package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Source;
import org.apache.http.HttpException;

import java.io.IOException;
import java.net.URISyntaxException;

public class SourceClientController extends AbstractClientController {
    public static void createSource(Source source) throws HttpException, IOException, URISyntaxException {
        var response = postRequest("/api/source", source);
        source.setId(Long.parseLong(response));
    }
}
