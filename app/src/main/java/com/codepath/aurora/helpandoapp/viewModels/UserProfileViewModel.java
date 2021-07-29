package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codepath.aurora.helpandoapp.models.Task;
import com.google.android.gms.maps.model.LatLng;
import com.hadiidbouk.charts.BarData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class UserProfileViewModel extends ViewModel {

    private ParseUser _user;
    private MutableLiveData<List<ParseObject>> _tasksCompleted;
    private MutableLiveData<Integer> _points;
    private ArrayList<BarData> _dataList = new ArrayList<>();
    private long _max;

    public long getMax() {
        return _max;
    }

    public ArrayList<BarData> getDataList() {
        return _dataList;
    }

    public ParseUser getUser() {
        return _user;
    }

    public void setUser(ParseUser user) {
        this._user = user;
    }

    public MutableLiveData<List<ParseObject>> getTasksCompleted() {
        if (_tasksCompleted == null) {
            _tasksCompleted = new MutableLiveData<>();
            _tasksCompleted.setValue(new ArrayList<ParseObject>());
            getUserTasksCompleted(_user); // Get the tasks from backend server
        }
        return _tasksCompleted;
    }

    public void setTasksCompleted(List<ParseObject> tasksCompleted) {
        this._tasksCompleted.postValue(tasksCompleted); // postValue because it is not called from the main thread
    }

    public MutableLiveData<Integer> getPoints() {
        if (_points == null) { // If null
            _points = new MutableLiveData<>();
            _points.setValue(new Integer(0));
            getUserPoints(_user); // Get the points from backend server
        }
        return _points;
    }

    public void setPoints(Integer points) {
        this._points.postValue(points); // postValue because it is not called from the main thread
    }

    /**
     * Gets all the tasks completed by a given user.
     *
     * @param idUser
     */
    public void getUserTasksCompleted(ParseUser idUser) {
        // Set up the query to obtain only id tasks completed by the user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TaskCompleted");
        query.whereEqualTo("user", idUser);
        query.include("task");
        query.orderByAscending("createdAt");
        // Clean dataList
        _dataList.clear();
        // Execute the query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> tasks, ParseException e) {
                if (e != null) {
                    return;
                }
                Date dateAux = new Date();
                BarData data;
                Integer pointsPerDay = new Integer(0);
                long max = 0;
                // Get the amount of point in each day worked
                for (int i = 0; i < tasks.size(); i++) {
                    // The specific task that was done
                    Task task = (Task) tasks.get(i).getParseObject("task");
                    // Date when it was completed
                    Date dateTask = tasks.get(i).getCreatedAt();
                    // Points of this specific task
                    Integer points = (Integer) task.getNumber("points");
                    if (sameDay(dateAux, dateTask) || i == 0) { // If this task has the same date than the previous one
                        pointsPerDay += points; // Points for that day increase
                    } else {
                        // Add this day as a new bar inside the chart
                        data = new BarData("      "+getSimpleDateString(dateAux), pointsPerDay, pointsPerDay+"\nPoints");
                        if (pointsPerDay > max)
                            max = pointsPerDay; // The max amount of point earned in one day
                        _dataList.add(data);
                        //Initialize the counter for a new date
                        pointsPerDay = points;
                    }
                    // Save the date to compare with the next one and see if they are the same day
                    dateAux = dateTask;
                }
                if (!pointsPerDay.equals(0)) {
                    data = new BarData("      "+getSimpleDateString(dateAux), pointsPerDay, pointsPerDay+"\nPoints");
                    if (pointsPerDay > max) max = pointsPerDay;
                    _dataList.add(data);
                }
                _max = max;
                _tasksCompleted.postValue(tasks);
            }

            /**
             * Return as a String the date with the default TimeZone for the host.
             * @param date
             * @return
             */
            private String getSimpleDateString(Date date) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = (calendar.get(Calendar.MONTH)) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                return year + "/" + (month < 10 ? "0" : "") + month + "/" + (day < 10 ? "0" : "") + day;
            }
        });
    }

    private boolean sameDay(Date dateAux, Date dateTask) {
        if (dateAux.getDay() != dateTask.getDay()) return false;
        if (dateAux.getMonth() != dateTask.getMonth()) return false;
        if (dateAux.getYear() != dateTask.getYear()) return false;
        return true;
    }


    /**
     * Gets user's points from the table TaskCompleted
     *
     * @param idUser
     */
    public void getUserPoints(ParseUser idUser) {
        // Set up the query to obtain the points
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TaskCompleted");
        query.whereEqualTo("user", idUser);
        query.include("task");
        // Execute the query
        List<ParseObject> tasks = new ArrayList<>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    return;
                }
                tasks.addAll(objects);
                Integer totalPoints = new Integer(0);
                for (ParseObject task : tasks) {
                    totalPoints += (Integer) task.getParseObject("task").getNumber("points");
                }
                _points.postValue(totalPoints);
            }
        });
    }
}