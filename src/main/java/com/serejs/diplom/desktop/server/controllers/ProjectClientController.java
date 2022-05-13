package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.text.container.View;
import com.serejs.diplom.desktop.ui.states.State;
import com.serejs.diplom.desktop.utils.GoogleSearchEngine;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectClientController extends AbstractClientController {
    public static void createProject(Project project) {
        try {
            var resp = postRequest("/api/project", project);
            var id = Long.parseLong(resp);
            project.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteProject(Project project) throws HttpException, IOException, URISyntaxException {
        deleteRequest("/api/project/" + project.getId());
    }

    public static LinkedList<Project> getProjects(View view) {
        var viewID = view.getId();
        var projects = new LinkedList<Project>();

        JSONArray jsonProjects;
        try {
            var params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("viewId", String.valueOf(viewID)));

            jsonProjects = new JSONArray(getRequest("/api/projects", params));
        } catch (Exception e) {
            return projects;
        }

        for (int i = 0; i < jsonProjects.length(); i++) {
            var jsonProject = jsonProjects.getJSONObject(i);

            var id = jsonProject.getBigInteger("id").longValue();
            var title = jsonProject.getString("title");
            var date = Date.valueOf(jsonProject.getString("date"));
            projects.add(new Project(id, title, view, date));
        }

        return projects;
    }
}
