package com.codepath.aurora.helpandoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.aurora.helpandoapp.adapters.OrganizationAdapter;
import com.codepath.aurora.helpandoapp.databinding.OrganizationsFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Organization;
import com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrganizationsFragment extends Fragment {
    private OrganizationsFragmentBinding _binding;
    private OrganizationsViewModel _viewModel;
    private OrganizationAdapter _adapter;
    private List<Organization> _orgs;


    public static OrganizationsFragment newInstance() {
        return new OrganizationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = OrganizationsFragmentBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(getActivity()).get(OrganizationsViewModel.class);
        _orgs = new ArrayList<>();
        setUpRecyclerView();
        setUpAllTheObservers();
    }

    /**
     * Set Up all the observers to listen the changes inside the View Model and update the UI
     * Specifically to listen when the list that has all the nonprofits organizations is updated
     */
    private void setUpAllTheObservers() {
        _viewModel.getOrgs().observe(getViewLifecycleOwner(), new Observer<List<Organization>>() {
            @Override
            public void onChanged(List<Organization> organizations) {
                if (_viewModel.getOrgs().getValue() != null) {
                    _orgs.clear();
                    _orgs.addAll(organizations);
                    // Notify the adapter of data change
                    _adapter.notifyDataSetChanged();
                }

            }
        });
    }

    /**
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvOrganizations.setLayoutManager(new LinearLayoutManager(_binding.getRoot().getContext()));
        _adapter = new OrganizationAdapter(_binding.getRoot().getContext(), _orgs);
        _binding.rvOrganizations.setAdapter(_adapter);
    }

}