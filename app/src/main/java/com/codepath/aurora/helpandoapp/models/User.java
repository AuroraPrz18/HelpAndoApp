package com.codepath.aurora.helpandoapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TYPE= "type";
    public static final String KEY_POINTS= "points";

    // Required empty constructor
    public User() {
    }

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

        //-------- Add row in TaskCompleted table --------------/
        Integer points = (Integer)currentUser.getNumber(User.KEY_POINTS);
        points += (Integer) ((int)task.getPoints()) ;
        currentUser.put(User.KEY_POINTS, points);
        // Saves the object.
        currentUser.saveInBackground();

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
}
