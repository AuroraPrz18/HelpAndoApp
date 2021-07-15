package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.aurora.helpandoapp.databinding.ActivityAddTaskBinding;
import com.codepath.aurora.helpandoapp.models.Task;


public class AddTaskActivity extends AppCompatActivity {
    ActivityAddTaskBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
    }

    public void onClickAddTask(View view) {
        addTask();
    }

    private void addTask() {
        Task newTask = new Task();
        newTask.setName(_binding.etName.getText().toString());
        newTask.setDescription(_binding.etDescription.getText().toString());
        newTask.setCategory(getCategory());
        newTask.setPoints(Long.parseLong(_binding.etPoint.getText().toString()));
    }

    private String getCategory() {
        return "Category";
    }
}