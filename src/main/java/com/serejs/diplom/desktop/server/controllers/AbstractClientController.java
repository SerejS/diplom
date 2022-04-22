package com.serejs.diplom.desktop.server.controllers;


import com.google.gson.Gson;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;


public abstract class AbstractClientController {
    protected static final String baseUrl = "http://localhost:8080";

    private static String request(String method, String endpoint, List<NameValuePair> params, Object body)
            throws IOException, HttpException, URISyntaxException {

        var user = State.getUser();
        var name = user.getUsername();
        var pass = user.getPassword();

        HttpClient httpclient = HttpClients.createDefault();

        var requestBuilder = new URIBuilder(baseUrl + endpoint);

        //Выбор метода запроса
        HttpRequestBase request;
        if (method.equals("GET")) {
            request = new HttpGet(requestBuilder.build());
            if (params != null) requestBuilder.addParameters(params);
        } else if (method.equals("POST")) {
            Gson gson = new Gson();

            request = new HttpPost(requestBuilder.build());
            ((HttpPost) request).setEntity(new StringEntity(gson.toJson(body)));
        } else throw new MethodNotSupportedException("Метод не поддерживается");



        // Заголовок запроса
        String encoding = Base64.getEncoder().encodeToString((name + ":" + pass).getBytes(StandardCharsets.UTF_8));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        HttpResponse response = httpclient.execute(request);

        var responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == HttpStatus.SC_UNAUTHORIZED)
            throw new HttpException("Ошибка авторизации. " + responseCode);

        try (var is = response.getEntity().getContent()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder jsonText = new StringBuilder();

            int cp;
            while ((cp = rd.read()) != -1)
                jsonText.append((char) cp);

            return jsonText.toString();
        }
    }

    protected static String getRequest(String endpoint, List<NameValuePair> params)
            throws HttpException, IOException, URISyntaxException {
        return request("GET", endpoint, params, null);
    }

    protected static String postRequest(String endpoint, Object body)
            throws HttpException, IOException, URISyntaxException {
        return request("POST", endpoint, null, body);
    }
}
