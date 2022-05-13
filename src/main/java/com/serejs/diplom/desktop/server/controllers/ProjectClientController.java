package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.text.container.Project;
import com.serejs.diplom.desktop.text.container.View;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.LinkedList;

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

    public static LinkedList<Project> getProjects(View view) throws HttpException, IOException, URISyntaxException {
        var viewID = view.getId();
        var projects = new LinkedList<Project>();

        var params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("viewId", String.valueOf(viewID)));

        var jsonProjects = new JSONArray(getRequest("/api/projects", params));

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
