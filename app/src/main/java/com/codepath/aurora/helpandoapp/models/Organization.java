package com.codepath.aurora.helpandoapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Organization {
    private int id;
    private int activeProjects;
    private int totalProjects;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
    private String ein;
    private String logoUrl;
    private String mission;
    private String name;
    private String postal;
    private String state;
    private String url;
    // Themes and countries are available in XML

    /***
     * Returns a list of Organization objects from a JSON Array
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    public static List<Organization> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Organization> organizations = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            organizations.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return organizations;
    }

    /**
     * Builds an Organization with the information provided inside a JSON Object
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private static Organization fromJson(JSONObject jsonObject) throws JSONException {
        Organization org = new Organization();
        if (jsonObject.has("activeProjects"))
            org.setActiveProjects(jsonObject.getInt("activeProjects"));
        if (jsonObject.has("addressLine1"))
            org.setAddressLine1(jsonObject.getString("addressLine1"));
        if (jsonObject.has("addressLine2"))
            org.setAddressLine2(jsonObject.getString("addressLine2"));
        if (jsonObject.has("city"))
            org.setCity(jsonObject.getString("city"));
        if (jsonObject.has("country"))
            org.setCountry(jsonObject.getString("country"));
        if (jsonObject.has("ein"))
            org.setEin(jsonObject.getString("ein"));
        if (jsonObject.has("logoUrl"))
            org.setLogoUrl(jsonObject.getString("logoUrl"));
        if (jsonObject.has("mission"))
            org.setMission(jsonObject.getString("mission"));
        if (jsonObject.has("name"))
            org.setName(jsonObject.getString("name"));
        if (jsonObject.has("postal"))
            org.setPostal(jsonObject.getString("postal"));
        if (jsonObject.has("state"))
            org.setState(jsonObject.getString("state"));
        if (jsonObject.has("totalProjects"))
            org.setTotalProjects(jsonObject.getInt("totalProjects"));
        if (jsonObject.has("url"))
            org.setUrl(jsonObject.getString("url"));
        return org;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(int activeProjects) {
        this.activeProjects = activeProjects;
    }

    public int getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
