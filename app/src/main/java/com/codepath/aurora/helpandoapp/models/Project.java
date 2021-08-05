package com.codepath.aurora.helpandoapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public final static String KEY_ACTIVITIES = "activities";
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
    public final static String KEY_SUMMARY = "summary";
    public final static String KEY_FUNDING = "funding";
    public final static String KEY_PROJECT_LINK = "projectLink";

    private int _id;
    private String _primaryTheme;
    private String _title;
    private String _c_contactAdress1;
    private String _c_contactAdress2;
    private String _c_city;
    private String _c_country;
    private String _c_email;
    private String _c_title;
    private String _c_url;
    private String _summary;
    private double _goal;
    private double _funding;
    private String _info_url;
    private String _projectLink;

    public String getProjectLink() {
        return _projectLink;
    }

    public void setProjectLink(String _projectLink) {
        this._projectLink = _projectLink;
    }

    public String getSummary() {
        return _summary;
    }

    public void setSummary(String _summary) {
        this._summary = _summary;
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

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getCContactAdress1() {
        return _c_contactAdress1;
    }

    public void setCContactAdress1(String _c_contactAdress1) {
        this._c_contactAdress1 = _c_contactAdress1;
    }

    public String getCContactAdress2() {
        return _c_contactAdress2;
    }

    public void setCContactAdress2(String _c_contactAdress2) {
        this._c_contactAdress2 = _c_contactAdress2;
    }

    public String getCCity() {
        return _c_city;
    }

    public void setCCity(String _c_city) {
        this._c_city = _c_city;
    }

    public String getCCountry() {
        return _c_country;
    }

    public void setCCountry(String _c_country) {
        this._c_country = _c_country;
    }

    public String getCEmail() {
        return _c_email;
    }

    public void setCEmail(String _c_email) {
        this._c_email = _c_email;
    }

    public String getCTitle() {
        return _c_title;
    }

    public void setCTitle(String _c_title) {
        this._c_title = _c_title;
    }

    public String getCUrl() {
        return _c_url;
    }

    public void setCUrl(String _c_url) {
        this._c_url = _c_url;
    }

    public double getGoal() {
        return _goal;
    }

    public void setGoal(double _goal) {
        this._goal = _goal;
    }

    public double getFunding() {
        return _funding;
    }

    public void setFunding(double _funding) {
        this._funding = _funding;
    }

    public String getInfo_url() {
        return _info_url;
    }

    public void setInfo_url(String _info_url) {
        this._info_url = _info_url;
    }



    /***
     * Build a list of Project objects from a JSON Array, and save it in a static variable
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    public static List<Project> jsonArrayToObjects(JSONArray jsonArray) throws JSONException {
        List<Project> allProjectsAux = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            allProjectsAux.add(jsonToObject(jsonArray.getJSONObject(i)));
        }
        return allProjectsAux;
    }

    /**
     * Builds a Project with the information provided inside a JSON Object
     * @param json
     * @return
     * @throws JSONException
     */
    public static Project jsonToObject(JSONObject json) throws JSONException {
        Project project = new Project();
        project.setId(json.getInt(KEY_ID));
        project.setPrimaryTheme(json.getString(KEY_PRIMARY_THEME));
        project.setCContactAdress1(json.getString(KEY_C_ADDRESS));
        project.setCCity(json.getString(KEY_C_CITY));
        project.setCCountry(json.getString(KEY_C_COUNTRY));
        if(json.has(KEY_C_EMAIL))
            project.setCEmail(json.getString(KEY_C_EMAIL));
        project.setTitle(json.getString(KEY_TITLE));
        project.setCUrl(json.getString(KEY_C_URL));
        if(json.has(KEY_URL))
            project.setInfo_url(json.getString(KEY_URL));
        project.setGoal(json.getDouble(KEY_GOAL));
        project.setFunding(json.getDouble(KEY_FUNDING));
        project.setSummary(json.getString(KEY_SUMMARY));
        project.setProjectLink(json.getString(KEY_PROJECT_LINK));
        return project;
    }
}
