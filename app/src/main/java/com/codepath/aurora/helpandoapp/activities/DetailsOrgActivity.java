package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.aurora.helpandoapp.GlideApp;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.adapters.ProjectAdapter;
import com.codepath.aurora.helpandoapp.databinding.ActivityDetailsOrgBinding;
import com.codepath.aurora.helpandoapp.models.Organization;
import com.codepath.aurora.helpandoapp.models.Project;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel.apiKey;
import static com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel.host;

public class DetailsOrgActivity extends AppCompatActivity {
    private Organization _org;
    private ActivityDetailsOrgBinding _binding;
    private List<Project> _projects;
    private ProjectAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityDetailsOrgBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        // Retrieve the Organization that was send
        if (getIntent() != null && getIntent().hasExtra("Org")) {
            _org = (Organization) Parcels.unwrap(getIntent().getParcelableExtra("Org"));
            buildUIWithTheData();
        }
        _projects = new ArrayList<>();
        setUpRecyclerView();
    }

    /**
     * Display the information related with this Organization
     */
    private void buildUIWithTheData() {
        _binding.tvName.setText(_org.getName());
        String themes = "";
        List<String> themesL = _org.getThemes();
        for (int i = 0; i < themesL.size(); i++) {
            if (i > 0) themes += ", ";
            themes += themesL.get(i);
        }
        _binding.tvThemes.setText(themes);
        if (!_org.getMission().isEmpty()) {
            _binding.tvMission.setText(_org.getMission());
        } else {
            _binding.tvMission.setVisibility(View.GONE);
            _binding.tvTitleMission.setVisibility(View.GONE);
        }
        String address = _org.getAddressLine1() + (_org.getAddressLine2().length() > 0 ? "\n" : "") + _org.getAddressLine2();
        _binding.tvAddress.setText(address);
        _binding.tvLocation.setText(_org.getCity() + (_org.getState().length() > 0 ? ", " : "") + _org.getState() + (_org.getCountry().length() > 0 ? ", " : "") + _org.getCountry());
        _binding.tvUrl.setText(_org.getUrl());
        String urlLogo = _org.getLogoUrl();
        // Loads the Logo with Glide into ivLogo
        GlideApp.with(this)
                .load(urlLogo)
                .fitCenter()
                .error(getDrawable(R.drawable.ic_business))
                .into(_binding.ivLogo);
        _binding.tvCountries.setText(Organization.getCountriesAsString(_org));
        getProjects();
    }

    /**
     * Retrieve all the active projects from this API
     */
    public void getProjects() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("Accept", "application/json"); // To get the response in JSON format - XML is the default value
        String url = getURLProjects(apiKey, _org.getId());
        Log.d("Projects", url);
        client.get(url, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONObject projects = json.jsonObject.getJSONObject("projects");
                    JSONArray projectsArray = projects.getJSONArray("project");
                    _projects.clear();
                    _projects.addAll(Project.jsonArrayToObjects(projectsArray));
                    _adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("Error", response);
            }
        });
    }

    public String getURLProjects(String apiKey, int id) {
        return host + "/api/public/projectservice/organizations/"+id+"/projects/active?api_key=" + apiKey;
    }

    /**
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvProjects.setLayoutManager(new LinearLayoutManager(_binding.getRoot().getContext()));
        _adapter = new ProjectAdapter(_binding.getRoot().getContext(), _projects);
        _binding.rvProjects.setAdapter(_adapter);
    }
}