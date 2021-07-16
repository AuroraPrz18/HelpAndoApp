package com.codepath.aurora.helpandoapp.fragments;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.AddTaskActivity;
import com.codepath.aurora.helpandoapp.adapters.TaskAdapter;
import com.codepath.aurora.helpandoapp.databinding.ToDoFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.viewModels.ToDoViewModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ToDoFragment extends Fragment {
    private ToDoFragmentBinding _binding;
    private ToDoViewModel _viewModel;
    private TaskAdapter _adapter;
    private List<Task> _tasks;

    public static ToDoFragment newInstance() {
        return new ToDoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = ToDoFragmentBinding.inflate(inflater, container, false);
        setUpRecyclerView();
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set onClickListener to the button which display AddTaskActivity to add a new task
        _binding.fabSuggestTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddTask Activity
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });
        setUpDropdownMenu();
    }

    /**
     * Populates Tasks list retrieving them from de backend server
     */
    private void populateTasksList(long minimum, long maximum) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo(Task.KEY_APPROVED, true); // Only tasks accepted
        query.whereGreaterThan(Task.KEY_POINTS, minimum);
        query.whereLessThan(Task.KEY_POINTS, maximum);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> receivedTasks, ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Save received tasks in tasks list
                _tasks.clear();
                _tasks.addAll(receivedTasks);
                // Notify the adapter of data change
                _adapter.notifyDataSetChanged();
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
                if (_binding.acChooser.getText().toString().equals(options[0])) { // Week
                    populateTasksList(0, 499); // Tasks which its points are between 0-499
                } else if (_binding.acChooser.getText().toString().equals(options[1])) { // Month
                    populateTasksList(500, 999); // Tasks which its points are between 500-999
                } else {
                    populateTasksList(1000, 10000); // Tasks which its points are between 1000-10000
                }
            }
        });
        _binding.acChooser.setText(options[0], false); // Default value selected
        populateTasksList(0, 499);
    }


    /**
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvTasks.setLayoutManager(new LinearLayoutManager(_binding.getRoot().getContext()));
        _tasks = new ArrayList<>();
        _adapter = new TaskAdapter(_tasks, _binding.getRoot().getContext());
        _binding.rvTasks.setAdapter(_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        // TODO: Use the ViewModel
    }

}