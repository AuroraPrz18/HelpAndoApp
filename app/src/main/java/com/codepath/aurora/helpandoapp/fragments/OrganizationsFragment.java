package com.codepath.aurora.helpandoapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.aurora.helpandoapp.Filter;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.MapsActivity;
import com.codepath.aurora.helpandoapp.adapters.OrganizationAdapter;
import com.codepath.aurora.helpandoapp.databinding.OrganizationsFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Organization;
import com.codepath.aurora.helpandoapp.models.User;
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
        if(User.userLocation==null){
            getLocation();
        }

    }

    /**
     * Get the locationof the user to find the perfect match for him/her
     */
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Request permission
        }
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("Title", getResources().getString(R.string.where_are_you));
        getActivity().startActivityForResult(intent, 100);
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
                    _viewModel.setUserUpdate(false);
                }
            }
        });
        _viewModel.getUserUpdate().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean locationUserHasBeenUpdated) {
                if(locationUserHasBeenUpdated){
                    // Filter it to match the user
                    new AsyncFilter().execute();
                    _viewModel.setUserUpdate(false);
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

    // The types specified here are the input data type, the progress type, and the result type
    private class AsyncFilter extends AsyncTask {
        private List<Organization> _orgsAsync;

        @Override
        protected Object doInBackground(Object[] objects) {
            _orgsAsync = new Filter(_orgs).execute();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            _orgs = _orgsAsync;
            // Notify the adapter of data change
            _adapter.notifyDataSetChanged();
            Log.d("filter", "finish");
        }
    }
}