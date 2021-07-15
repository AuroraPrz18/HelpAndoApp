package com.codepath.aurora.helpandoapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Task")
public class Task extends ParseObject {
    public static final String KEY_NAME = "userName";
    public static final String KEY_DESCRIPTION = "userName";
    public static final String KEY_CATEGORY = "userName";
    public static final String KEY_POINTS = "userName";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public long getPoints() {
        return (long)getNumber(KEY_POINTS);
    }

    public void setPoints(long points) {
        put(KEY_POINTS, points);
    }

}
