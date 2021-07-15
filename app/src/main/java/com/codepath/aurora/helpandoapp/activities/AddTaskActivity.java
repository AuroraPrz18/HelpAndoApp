package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityAddTaskBinding;
import com.codepath.aurora.helpandoapp.models.Task;
import com.parse.ParseUser;


public class AddTaskActivity extends AppCompatActivity {
    ActivityAddTaskBinding _binding;
    private int _category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        // Setup the dropdown menu
        _category = -1; //TODO: Change this variable to the ViewModel
        _binding.acCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // _userType saves the last user type clicked
                _category = position;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        populateDropdownMenuCategories();
    }

    /**
     * Method to retrieve the different user types and populate the Dropdown Menu whit them.
     */
    private void populateDropdownMenuCategories() {
        String [] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories);
        _binding.acCategory.setAdapter(adapter);
    }

    public void onClickAddTask(View view) {
        addTask();
    }

    private void addTask() {
        Task newTask = new Task();
        newTask.setUser(ParseUser.getCurrentUser());
        newTask.setName(_binding.etName.getText().toString());
        newTask.setDescription(_binding.etDescription.getText().toString());
        newTask.setCategory(getCategory());
        newTask.setPoints(Long.parseLong(_binding.etPoint.getText().toString()));
        addTaskInBackground(newTask);
    }

    private void addTaskInBackground(Task newTask) {
        // Saves the new object.
        newTask.saveInBackground(e -> {
            if(e != null){ // Something returns a exception
                Toast.makeText(AddTaskActivity.this, "Something went wrong "+e, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(AddTaskActivity.this, "Your task has been submitted!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private String getCategory() {
        String [] categories = getResources().getStringArray(R.array.categories);
        return categories[_category];
    }
}