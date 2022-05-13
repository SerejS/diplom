package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class EngineClientController extends AbstractClientController {

    public static List<GoogleSearchEngine> getEngines(Project project) throws HttpException, IOException, URISyntaxException {
        LinkedList<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("projectId", String.valueOf(project.getId())));

        var engines = new LinkedList<GoogleSearchEngine>();
        var response = getRequest("/api/engines", params);

        var list = new JSONArray(response);
        for (int i = 0; i < list.length(); i++) {
            var jsonObj = list.getJSONObject(i);

            var gse = new GoogleSearchEngine(
                    jsonObj.getLong("id"),
                    jsonObj.getString("cx"),
                    jsonObj.getString("token"),
                    State.getLitTypeById(jsonObj.getJSONObject("type").getLong("id")),
                    project
            );

            engines.add(gse);
        }
        return engines;
    }

    //Отправление поисковых движков на сервер
    public static void sendEngines(List<GoogleSearchEngine> engines) throws HttpException, IOException, URISyntaxException {
        postRequest("/api/engines", engines);
    }
}
