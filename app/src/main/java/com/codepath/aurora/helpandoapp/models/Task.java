package com.codepath.aurora.helpandoapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze = Task.class) // Define explicitly what types Parceler should analyze, to avoid issues because this class extends ParseObject
@ParseClassName("Task")
public class Task extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_POINTS = "points";
    public static final String KEY_APPROVED = "isApproved";

    public Task() {
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

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
        return ((Number) getNumber(KEY_POINTS)).longValue();
    }

    public void setPoints(long points) {
        put(KEY_POINTS, points);
    }

    public boolean getIsApproved() {
        return getBoolean(KEY_APPROVED);
    }

    public void setIsApproved(boolean isApproved) {
        put(KEY_APPROVED, isApproved);
    }

}
