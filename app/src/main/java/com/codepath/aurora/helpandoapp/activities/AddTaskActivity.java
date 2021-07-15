package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityAddTaskBinding;
import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.viewModels.AddTaskViewModel;
import com.parse.ParseUser;


public class AddTaskActivity extends AppCompatActivity {
    private ActivityAddTaskBinding _binding;
    private AddTaskViewModel _viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        // Creates a ViewModel the first time the system calls an activity's onCreate() method
        // On the other hand, if the activity is re-created it receives the same viewModel than the first Activity
        _viewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        setUpDropDownMenu();
    }

    /***
     * Sets the onItemClickListener to save the value selected in a variable and use it to know what
     * option was selected
     */
    private void setUpDropDownMenu() {
        _binding.acCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // _userType saves the last category clicked
                _viewModel.setCategory(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDropdownMenuCategories();
    }

    /**
     * Method to retrieve the different categories and populate the Dropdown Menu whit them.
     */
    private void populateDropdownMenuCategories() {
        // Get all categories from the resources (strings.xml)
        String [] categories = getResources().getStringArray(R.array.categories);
        // Create an adapter which will help to bind the strings with the menu
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories);
        // Set the menu's adapter
        _binding.acCategory.setAdapter(adapter);
    }

    public void onClickAddTask(View view) {
        if(isValid()){ // If all the data provided is complete
            addTaskInBackground(createTask());
        }else{
            Toast.makeText(AddTaskActivity.this, getResources().getString(R.string.fields_required), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates a tasks with the information provided by the user
     */
    private Task createTask() {
        Task newTask = new Task();
        // Adds the information required
        newTask.setUser(ParseUser.getCurrentUser());
        newTask.setName(_binding.etName.getText().toString());
        newTask.setDescription(_binding.etDescription.getText().toString());
        newTask.setCategory(getCategory());
        newTask.setPoints(Long.parseLong(_binding.etPoint.getText().toString()));
        return newTask;
    }

    /**
     * Checks if the information provided is complete
     * @return True if it is complete
     */
    private boolean isValid() {
        // TODO: Check if the information provided is correct
        return true;
    }

    /**
     * Adds a task given to the backend server
     * @param newTask
     */
    private void addTaskInBackground(Task newTask) {
        // Saves the new object.
        newTask.saveInBackground(e -> {
            if(e != null){ // Something went wrong
                Toast.makeText(AddTaskActivity.this, getResources().getString(R.string.wrong)+e, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(AddTaskActivity.this, getResources().getString(R.string.success_task), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /**
     * Returns the category selected in a String format
     * @return
     */
    private String getCategory() {
        String [] categories = getResources().getStringArray(R.array.categories);
        return categories[_viewModel.getCategory()];
    }
}