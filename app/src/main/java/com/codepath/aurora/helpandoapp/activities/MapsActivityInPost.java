package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityMapsInPostBinding;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

public class MapsActivityInPost extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap _map;
    private ActivityMapsInPostBinding _binding;
    private PlaceP _place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityMapsInPostBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        if (getIntent() != null) { // It receives a place to mark
            _place = (PlaceP) Parcels.unwrap(getIntent().getParcelableExtra("Place"));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // It manages the life cycle of the map and is the parent of the app's UI
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        _map = googleMap;
        // Get the Lat and Lng
        LatLng latLng = getLatLng();
        // Add a marker in the place and move the camera
        _map.addMarker(new MarkerOptions().position(latLng).title(getResources().getString(R.string.place)));
        _map.moveCamera(CameraUpdateFactory.newLatLng(latLng)); // Centers the map at the LatLng given
        // Set Up the map
        _map.getUiSettings().setCompassEnabled(true);
        _map.getUiSettings().setMapToolbarEnabled(true);
        _map.getUiSettings().setZoomGesturesEnabled(true);

    }

    /**
     * Return latitude and Longitude from an specific place
     *
     * @return
     */
    private LatLng getLatLng() {
        String latLng = _place.getLatLng();
        latLng = latLng.substring(10, latLng.length() - 1);
        String[] latlongA = latLng.split(",");
        double latitude = Double.parseDouble(latlongA[0]);
        double longitude = Double.parseDouble(latlongA[1]);
        return new LatLng(latitude, longitude);
    }
}