package com.codepath.aurora.helpandoapp.models;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Organization {
    public static String KEY_ID = "id";
    public static String KEY_ACT_PROJ = "activeProjects";
    public static String KEY_TOTAL_PROJ = "totalProjects";
    public static String KEY_ADDRESS1 = "addressLine1";
    public static String KEY_ADDRESS2 = "addressLine2";
    public static String KEY_CITY = "city";
    public static String KEY_COUNTRY = "country";
    public static String KEY_EIN = "ein";
    public static String KEY_LOGO = "logoUrl";
    public static String KEY_MISSION = "mission";
    public static String KEY_NAME = "name";
    public static String KEY_POSTAL = "postal";
    public static String KEY_STATE = "state";
    public static String KEY_URL = "url";
    public static String KEY_COUNTRIES = "countries";
    public static String KEY_THEMES = "themes";

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
    private List<String> countries; // Check, it can be duplicated - Countries where the organization operates in
    private List<String> themes; // Check, it can be duplicated - Countries where the organization operates in


    public Organization() {
        this.countries = new ArrayList<>();
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

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return " ->" + this.name ;

    }


}
