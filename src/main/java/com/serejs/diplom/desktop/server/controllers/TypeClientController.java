package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.text.container.View;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class TypeClientController extends AbstractClientController {

    public static List<LiteratureType> getTypes(long viewID) {
        List<LiteratureType> types = new LinkedList<>();

        String responseContent;
        try {
            var params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("viewId", String.valueOf(viewID)));

            responseContent = getRequest("/api/types", params);
        } catch (Exception e) {
            ErrorAlert.info("Ошибка получения типов литературы");
            return types;
        }

        var jsonLitType = new JSONArray(responseContent);
        for (int i = 0; i < jsonLitType.length(); i++) {
            var jsonLitTypes = jsonLitType.getJSONObject(i);

            var type = new LiteratureType(
                    jsonLitTypes.getLong("id"),
                    jsonLitTypes.getString("title"),
                    jsonLitTypes.getBoolean("main"));
            types.add(type);
        }

        return types;
    }
}
