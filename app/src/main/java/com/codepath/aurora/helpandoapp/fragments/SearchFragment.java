package com.codepath.aurora.helpandoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.adapters.UserAdapter;
import com.codepath.aurora.helpandoapp.databinding.SearchFragmentBinding;
import com.codepath.aurora.helpandoapp.models.User;
import com.codepath.aurora.helpandoapp.viewModels.SearchViewModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private SearchFragmentBinding _binding;
    private SearchViewModel mViewModel;
    private UserAdapter _adapter;
    private List<ParseUser> _users;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = SearchFragmentBinding.inflate(inflater, container, false);
        setUpRecyclerView();
        populateUsersList();
        return _binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    /**
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        _users = new ArrayList<>();
        _adapter = new UserAdapter(getContext(), _users);
        _binding.rvUsers.setAdapter(_adapter);
    }


    /**
     * Populates Users list retrieving them from de backend server
     */
    private void populateUsersList() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(User.KEY_TYPE, "Volunteer"); // Only Volunteers
        query.orderByDescending(User.KEY_POINTS);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> receivedUsers, ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Save received users
                _users.clear();
                _users.addAll(receivedUsers);
                // Notify the adapter of data change
                _adapter.notifyDataSetChanged();
            }
        });
    }
}