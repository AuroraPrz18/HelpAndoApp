package com.codepath.aurora.helpandoapp.models;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParseClassName("User")
public class User extends ParseObject {
    public static LatLng userLocation;
    public static String userCountry = "";
    public static String userCity = "";

    public static final String KEY_USERNAME = "userName";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TYPE = "type";
    public static final String KEY_POINTS = "points";
    public static final String KEY_TASKS_C = "tasksCompleted";
    public static final String KEY_TASKS_S = "tasksSuggested";
    public static final String KEY_EMAIL = "contactEmail";
    public static final String KEY_PHONE = "telephone";
    public static final String KEY_PROFILE_PHOTO = "profilePicture";

    // Required empty constructor
    public User() {
    }

    public static String[] getTop3() {
        List<ParseObject> tasks = new ArrayList<>();
        Map<String, Integer> mp = new HashMap<>();
        // Set up the query to obtain only id tasks completed by the user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TaskCompleted");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.include("task");
        // Execute the query
        try {
            tasks = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < tasks.size(); i++) {
            Task task = (Task) tasks.get(i).getParseObject("task");
            String theme = task.getCategory();
            if (mp.containsKey(theme)) {
                mp.put(theme, ((int) mp.get(theme)) + 1);
            } else {
                mp.put(theme, 1);
            }
        }
        String[] top3 = new String[3];
        for (int i = 0; i < 3; i++) {
            long max = 0;
            top3[i] = "";
            for (String key : mp.keySet()) {
                if (mp.get(key) > max) {
                    top3[i] = key;
                }
            }
            if (mp.containsKey(top3[i])) {
                mp.put(top3[i], -i - 1);
            }
        }
        Log.d("filter", top3[0] + " \n" + top3[1] + " \n" + top3[2]);
        return top3;
    }


    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getPassword() {
        return getString(KEY_PASSWORD);
    }

    public void setPassword(String password) {
        put(KEY_PASSWORD, password);
    }

    public String getType() {
        return getString(KEY_TYPE);
    }

    public void setType(String type) {
        put(KEY_TYPE, type);
    }

    public ParseFile getProfilePhoto() {
        return getParseFile(KEY_PROFILE_PHOTO);
    }

    public void setProfilePhoto(ParseFile photo) {
        put(KEY_PROFILE_PHOTO, photo);
    }

    public static void getCountry(Context context) {
        userCountry = PlaceP.getCountry(context, userLocation);
    }


    public static void getCity(Context context) {
        userCity = PlaceP.getCity(context, userLocation);
    }

    /**
     * Add a row in the TaskCompleted with a pointer to the user and the task, to make a relation between them.
     * Moreover, updates the points of the user.
     *
     * @param task
     */
    public static void newTaskCompleted(Task task) {
        //-------- Add row in TaskCompleted table --------------/
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) return;
        // Create an entry in the TaskCompleted table
        ParseObject newTaskCompleted = new ParseObject("TaskCompleted");
        // Set a relation in this table pointing the task and the user
        newTaskCompleted.put("task", task);
        newTaskCompleted.put("user", currentUser);
        // Save this in the backend server
        newTaskCompleted.saveInBackground();

        //-------- Update User's points --------------/
        Integer points = (Integer) currentUser.getNumber(User.KEY_POINTS);
        points += (Integer) ((int) task.getPoints());
        currentUser.put(User.KEY_POINTS, points);
        //-------- Update User's tasksCompleted -------/
        Integer tasksCompleted = (Integer) currentUser.getNumber(User.KEY_TASKS_C);
        currentUser.put(User.KEY_TASKS_C, tasksCompleted + 1);
        // Saves the object.
        currentUser.saveInBackground();
    }
}
