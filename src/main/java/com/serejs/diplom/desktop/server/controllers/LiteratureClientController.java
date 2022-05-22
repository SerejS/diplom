package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.FormatLiterature;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class LiteratureClientController extends AbstractClientController {
    private final static String endpoint = "/api/literatures";

    public static void sendLiterature(Literature literature) throws HttpException, IOException, URISyntaxException {
        if (literature instanceof FormatLiterature l) {
            var format = l.getFormat();
            FormatClientController.sendFormat(format);
        }

        var response = postRequest(endpoint, literature);
        literature.setId(Long.parseLong(response));
    }

    public static void sendLiteratures(List<Literature> literatures) throws HttpException, IOException, URISyntaxException {
        for (var literature : literatures) {
            sendLiterature(literature);
        }
    }

    public static List<Literature> getLiteratures(Project project) throws HttpException, IOException, URISyntaxException {
        var params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("projectId", String.valueOf(project.getId())));

        var literatures = new LinkedList<Literature>();

        var jsonLiteratures = new JSONArray(getRequest(endpoint, params));

        for (int i = 0; i < jsonLiteratures.length(); i++) {
            var jsonLiterature = jsonLiteratures.getJSONObject(i);

            var id = jsonLiterature.getBigInteger("id").longValue();
            var uri = new URI(jsonLiterature.getString("path"));
            var source = jsonLiterature.getEnum(SourceType.class, "source");

            var litTypeid = jsonLiterature.getLong("typeId");
            var litType = State.getLitTypeById(litTypeid);

            literatures.add(new Literature(
                    id, uri, source, litType, project
            ));
        }

        return literatures;
    }
}
