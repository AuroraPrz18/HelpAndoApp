package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.codepath.aurora.helpandoapp.models.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationsViewModel extends ViewModel {
    public List<Organization> orgs = new ArrayList<>();
    public static final String host = "https://api.globalgiving.org";
    public static final String organizations = "/api/public/orgservice/all/organizations/vetted";

    public String getURLOrganizations(String apiKey) {
        return host + organizations + "?api_key=" + apiKey;
    }
}