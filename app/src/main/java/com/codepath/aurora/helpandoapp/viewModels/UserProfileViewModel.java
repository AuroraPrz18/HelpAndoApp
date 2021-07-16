package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {
    private ParseUser _user;
    private MutableLiveData<List<ParseObject>> _tasksCompleted;
    private MutableLiveData<Integer> _points;


    public ParseUser getUser() {
        return _user;
    }

    public void setUser(ParseUser user) {
        this._user = user;
    }

    public MutableLiveData<List<ParseObject>> getTasksCompleted() {
        if(_tasksCompleted == null){
            _tasksCompleted = new MutableLiveData<>();
            _tasksCompleted.setValue(new ArrayList<ParseObject>());
            getUserTasksCompleted(_user);
        }
        return _tasksCompleted;
    }

    public void setTasksCompleted(List<ParseObject> tasksCompleted) {
        this._tasksCompleted.postValue(tasksCompleted); // postValue because it is not called from the main thread
    }

    public MutableLiveData<Integer> getPoints() {
        if(_points == null){
            _points = new MutableLiveData<>();
            _points.setValue(new Integer(0));
            getUserPoints(_user);
        }
        return _points;
    }

    public void setPoints(Integer points) {
        this._points.postValue(points); // postValue because it is not called from the main thread
    }

    public void getUserTasksCompleted(ParseUser idUser){
        // TODO: TEST IT MORE TIMES
        // Set up the query to obtain only id tasks completed by the user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TaskCompleted");
        query.whereEqualTo("user", idUser);
        query.include("task");
        // Execute the query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null){
                    return;
                }
                _tasksCompleted.postValue(objects);
            }
        });
    }


    public void getUserPoints(ParseUser idUser){

    }
}