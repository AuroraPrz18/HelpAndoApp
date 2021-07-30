package com.codepath.aurora.helpandoapp.models;

import com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Project {
    public final static String KEY_ACTIVITIES = "projects";
    public final static String KEY_URL = "additionalDocumentation"; //Optional
    public final static String KEY_C_ADDRESS = "contactAddress";
    public final static String KEY_C_CITY = "contactCity";
    public final static String KEY_C_COUNTRY = "contactCountry";
    public final static String KEY_C_EMAIL = "contactemail";
    public final static String KEY_C_TITLE = "contactTitle";
    public final static String KEY_C_URL = "contactUrl"; //-----------------------
    public final static String KEY_GOAL = "goal";
    public final static String KEY_ID = "id";
    public final static String KEY_PRIMARY_THEME = "themeName";
    public final static String KEY_TITLE = "title";

    private int _id;
    private String _primaryTheme;

    public Project(int _id, String _primaryTheme) {
        this._id = _id;
        this._primaryTheme = _primaryTheme;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getPrimaryTheme() {
        return _primaryTheme;
    }

    public void setPrimaryTheme(String _primaryTheme) {
        this._primaryTheme = _primaryTheme;
    }

    public static List<Project> jsonArrayToList(JSONArray projectsArray) throws JSONException {
        List<Project> projects = new ArrayList<>();
        for(int i=0; i<projectsArray.length(); i++){
            JSONObject project = projectsArray.getJSONObject(i);
            projects.add(new Project(project.getInt(KEY_ID), project.getString(KEY_PRIMARY_THEME)));
        }
        return projects;
    }


    /**
     * Retrieve all the active projects for a Organization
     * @param organizationId
     * @return
     */
    public static List<Project> getAllProjects(String organizationId){
        List<Project> projects = new ArrayList<>();
        // Instantiate OkHttpClient
        OkHttpClient client = new OkHttpClient();
        String url = OrganizationsViewModel.host +
                        OrganizationsViewModel.projects + "?api_key=" +
                        OrganizationsViewModel.apiKey + "&nextProjectId=" + organizationId;
        // Create a Request object
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                JSONObject projectsObj = new JSONObject(response.body().string()).getJSONObject("projects");
                JSONArray projectsArray = projectsObj.getJSONArray("project");
                projects = Project.jsonArrayToList(projectsArray);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
