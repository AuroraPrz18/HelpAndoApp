package com.codepath.aurora.helpandoapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TYPE = "type";
    public static final String KEY_POINTS = "points";
    public static final String KEY_TASKS_C = "tasksCompleted";
    public static final String KEY_TASKS_S = "tasksSuggested";
    public static final String KEY_EMAIL = "contactEmail";
    public static final String KEY_PHONE = "telephone";

    // Required empty constructor
    public User() {
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
