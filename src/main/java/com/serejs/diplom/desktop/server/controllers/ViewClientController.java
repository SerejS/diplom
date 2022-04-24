package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.View;
import org.apache.http.HttpException;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class ViewClientController extends AbstractClientController {
    public static List<View> getViews() throws Exception {
        var jsonViews = new JSONArray(getRequest("/api/views", null));

        var views = new LinkedList<View>();
        for (int i = 0; i < jsonViews.length(); i++) {
            var jsonView = jsonViews.getJSONObject(i);
            var view = new View(jsonView.getBigInteger("id").longValue(), jsonView.getString("title"));
            views.add(view);
        }

        return views;
    }

    public static View addView(String viewName) throws HttpException, IOException, URISyntaxException {
        var view = new View(-1L, viewName);
        var response = postRequest("/api/views", view);
        view.setId(Long.parseLong(response));
        return view;
    }
}
