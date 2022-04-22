package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.server.User;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserClientController extends AbstractClientController {

    public static void register(String username, String pass) throws IOException, HttpException {
        User registrationUser;
        try {
            registrationUser = new User(username, pass);
        } catch (Exception e) {
            ErrorAlert.info("Ошибка генерации пароля");
            return;
        }

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(baseUrl + "/user/registration");

        // Параметры запроса
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", registrationUser.getPassword()));
        httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        HttpResponse response = httpclient.execute(httppost);

        if (response.getStatusLine().getStatusCode() != 200) throw new HttpException("Ошибка регистрации");
    }


    public static void auth(String username, String password) throws Exception {
        State.setUser(new User(username, password));

        List<NameValuePair> body = new LinkedList<>();
        postRequest("/auth", null);
    }
}
