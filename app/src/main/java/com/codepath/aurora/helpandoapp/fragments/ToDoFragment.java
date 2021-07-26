package com.codepath.aurora.helpandoapp.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.AddTaskActivity;
import com.codepath.aurora.helpandoapp.adapters.TaskAdapter;
import com.codepath.aurora.helpandoapp.databinding.ToDoFragmentBinding;
import com.codepath.aurora.helpandoapp.viewModels.TasksViewModel;

import org.jetbrains.annotations.NotNull;

public class ToDoFragment extends Fragment {
    private ToDoFragmentBinding _binding;
    private TasksViewModel _viewModel;
    private TaskAdapter _adapter;
    private int miniPoints;
    private int maxiPoints;

    public static ToDoFragment newInstance() {
        return new ToDoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = ToDoFragmentBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        // Connect changes inside the ViewModel with the UI (to be automatically upload with them)
        setUpAllTheObservers();
        setUpDropdownMenu();
        setUpRecyclerView();
        // Set onClickListener to the button which display AddTaskActivity to add a new task
        _binding.fabSuggestTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddTask Activity
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Apply activity transition
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
                    // Swap without transition
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Method to retrieve the different options to filter, populate the Dropdown Menu whit them and set a listener to the dropdown menu.
     */
    private void setUpDropdownMenu() {
        // Set the different options to filter
        String[] options = getResources().getStringArray(R.array.order_by);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, options);
        _binding.acChooser.setAdapter(adapter);
        // Set onClickListener where depending on the option selected the tasks will change
        _binding.acChooser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String optionInSpinner = _binding.acChooser.getText().toString();
                if (optionInSpinner.equals(options[0])) { // Week
                    // Tasks which its points are between 0-499
                    miniPoints = 0;
                    maxiPoints = 499;
                } else if (optionInSpinner.equals(options[1])) { // Month
                    // Tasks which its points are between 500-999
                    miniPoints = 500;
                    maxiPoints = 999;
                } else {
                    // Tasks which its points are between 1000-10000
                    miniPoints = 1000;
                    maxiPoints = 10000;
                }
                _viewModel.populateTasksList(miniPoints, maxiPoints);
            }
        });
        _binding.acChooser.setText(options[0], false); // Default value selected
        miniPoints = 0;
        maxiPoints = 499;
    }

    /**
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        _adapter = new TaskAdapter(_viewModel.getTasks().getValue(), _binding.getRoot().getContext());
        _binding.rvTasks.setAdapter(_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AddTaskActivity.newTaskAdded) {
            _viewModel.setResultToOperation(_viewModel.NEW_TASK_ADDED);
        }
    }

    /**
     * Set Up all the observers to listen the changes inside the View Model and update the UI
     */
    private void setUpAllTheObservers() {
        // This also going to listen when the user create a new Task to update the UI in the moment
        _viewModel.getResultToOperation().observe(getViewLifecycleOwner(), new Observer<Byte>() {
            @Override
            public void onChanged(Byte resultToOperations) {
                if (resultToOperations == _viewModel.TASKS_OBTAINED) { // The tasks are ready
                    // Notify the adapter of data change
                    _adapter.notifyDataSetChanged();
                } else if (resultToOperations == _viewModel.NEW_TASK_ADDED) { // The user add a new task
                    _viewModel.populateTasksList(miniPoints, maxiPoints);
                } else if (resultToOperations == _viewModel.SOMETHING_WRONG_RETRIEVING_TASKS) { // Something went wrong
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                } else if (resultToOperations == _viewModel.TASKS_NOT_OBTAINED_YET) { // Tasks retrieve from the backend
                    _viewModel.populateTasksList(0, 499);
                }
            }
        });
    }
}