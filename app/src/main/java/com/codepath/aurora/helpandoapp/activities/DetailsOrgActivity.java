package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.aurora.helpandoapp.GlideApp;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityDetailsOrgBinding;
import com.codepath.aurora.helpandoapp.models.Organization;

import org.parceler.Parcels;

import java.util.List;

public class DetailsOrgActivity extends AppCompatActivity {
    private Organization _org;
    private ActivityDetailsOrgBinding _binding;

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
        String address = _org.getAddressLine1() + (_org.getAddressLine2().length()>0?"\n": "")+ _org.getAddressLine2();
        _binding.tvAddress.setText(address);
        _binding.tvLocation.setText(_org.getCity() + (_org.getState().length()>0?", ": "") + _org.getState() + (_org.getCountry().length()>0?", ": "") + _org.getCountry());
        _binding.tvUrl.setText(_org.getUrl());
        String urlLogo = _org.getLogoUrl();
        // Loads the Logo with Glide into ivLogo
        GlideApp.with(this)
                .load(urlLogo)
                .fitCenter()
                .error(getDrawable(R.drawable.ic_business))
                .into(_binding.ivLogo);
        _binding.tvCountries.setText(Organization.getCountriesAsString(_org));
    }
}