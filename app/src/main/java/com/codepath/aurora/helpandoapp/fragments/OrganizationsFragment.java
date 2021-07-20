package com.codepath.aurora.helpandoapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.adapters.OrganizationAdapter;
import com.codepath.aurora.helpandoapp.databinding.OrganizationsFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Organization;
import com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;

public class OrganizationsFragment extends Fragment {
    private OrganizationsFragmentBinding _binding;
    private OrganizationsViewModel _viewModel;
    private OrganizationAdapter _adapter;

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
        _viewModel = new ViewModelProvider(this).get(OrganizationsViewModel.class);
        setUpRecyclerView();
        populateOrganizations();
    }

    /**
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvOrganizations.setLayoutManager(new LinearLayoutManager(_binding.getRoot().getContext()));
        _viewModel.orgs = new ArrayList<>();
        _adapter = new OrganizationAdapter(_binding.getRoot().getContext(), _viewModel.orgs);
        _binding.rvOrganizations.setAdapter(_adapter);
    }

    /**
     * Executes the request asynchronously and fire the onSuccess when the response returns
     * a success code and onFailure if the response does not. Using AsyncHttpClient library.
     */
    private void populateOrganizations(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("Accept", "application/json");
        String url = _viewModel.getURLOrganizations(getResources().getString(R.string.api_organizations));
        Log.d("DEBUG ARRAY", url);
        client.get(url, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Access a JSON array response with `json.jsonArray`
                try {
                    JSONObject organizations = json.jsonObject.getJSONObject("organizations");
                    JSONArray organizationsArray = organizations.getJSONArray("organization");
                    // Save received Organizations
                    _viewModel.orgs.addAll(Organization.fromJsonArray(organizationsArray));
                    // Notify the adapter of data change
                    _adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                //Log.d("DEBUG ARRAY", response);
            }
        });
    }

}