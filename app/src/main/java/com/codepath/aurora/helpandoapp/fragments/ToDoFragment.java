package com.codepath.aurora.helpandoapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.codepath.aurora.helpandoapp.activities.AddTaskActivity;
import com.codepath.aurora.helpandoapp.databinding.ToDoFragmentBinding;
import com.codepath.aurora.helpandoapp.viewModels.ToDoViewModel;

import org.jetbrains.annotations.NotNull;

public class ToDoFragment extends Fragment {
    private ToDoFragmentBinding _binding;
    private ToDoViewModel mViewModel;

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
        _binding.fabSuggestTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddTask Activity
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        // TODO: Use the ViewModel
    }

}