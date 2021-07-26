package com.codepath.aurora.helpandoapp.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TasksViewModel extends ViewModel {
    private MutableLiveData<List<Task>> _tasks;
    private MutableLiveData<Byte> _resultToOperation;
    private int _category = -1; // When -1, any option has been selected

    // Types of results for operations
    public final byte TASKS_OBTAINED = (byte) 1;
    public final byte SOMETHING_WRONG_RETRIEVING_TASKS = (byte) 0;
    public final byte TASKS_NOT_OBTAINED_YET = (byte) -1;
    public final byte NEW_TASK_ADDED = (byte) 2;
    public final byte SOMETHING_WRONG_ADDING_TASK = (byte) -2;

    public int getCategory() {
        return _category;
    }

    public void setCategory(int category) {
        this._category = category;
    }

    public void setResultToOperation(byte _resultToOperation) {
        this._resultToOperation.setValue(_resultToOperation);
    }

    public LiveData<List<Task>> getTasks() {
        if (_tasks == null) {
            _tasks = new MutableLiveData<List<Task>>();
            _tasks.setValue(new ArrayList<>());
        }
        return _tasks;
    }

    public LiveData<Byte> getResultToOperation() {
        if (_resultToOperation == null) {
            _resultToOperation = new MutableLiveData<Byte>();
            _resultToOperation.setValue(TASKS_NOT_OBTAINED_YET);
        }
        return _resultToOperation;
    }

    /**
     * Populates Tasks list retrieving them from de backend server
     */
    public void populateTasksList(long minimum, long maximum) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo(Task.KEY_APPROVED, true); // Only tasks accepted
        query.whereGreaterThanOrEqualTo(Task.KEY_POINTS, minimum);
        query.whereLessThanOrEqualTo(Task.KEY_POINTS, maximum);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> receivedTasks, ParseException e) {
                if (e != null) {
                    _resultToOperation.postValue(SOMETHING_WRONG_RETRIEVING_TASKS);
                    return;
                }
                // Save received tasks in tasks list
                _tasks.getValue().clear();
                _tasks.getValue().addAll(receivedTasks);
                // Notify UI that hte tasks are ready
                _resultToOperation.postValue(TASKS_OBTAINED);
            }
        });
    }

    /**
     * Adds a task given to the backend server
     *
     * @param newTask
     */
    public void addTaskInBackground(Task newTask) {
        // Saves the new object.
        newTask.saveInBackground(e -> {
            if (e != null) { // Something went wrong
                _resultToOperation.postValue(SOMETHING_WRONG_ADDING_TASK);
                Log.e("ERROR", e + "");
                return;
            }
            _resultToOperation.postValue(NEW_TASK_ADDED);
            //-------- Update User's tasksSuggested -------/
            Integer tasksSuggested = (Integer) ParseUser.getCurrentUser().getNumber(User.KEY_TASKS_S);
            ParseUser.getCurrentUser().put(User.KEY_TASKS_S, tasksSuggested + 1);
        });
    }
}