package com.codepath.aurora.helpandoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityMapsBinding;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap _mMap;
    private ActivityMapsBinding _binding;
    private PlaceP _place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        populateDropdownMenu();

        _place = null;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // It manages the life cycle of the map and is the parent of the app's UI
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        _binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        _binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPlaceIfPossible();
            }
        });
    }

    /**
     * Returns the Place selected
     */
    private void sendPlaceIfPossible() {
        if(_place == null){
            Toast.makeText(MapsActivity.this, getResources().getString(R.string.fields_required), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("Place", Parcels.wrap(_place));
        setResult(100,  intent);
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        _mMap = googleMap;
        setUpAutoCompleteSearcher();
    }

    /**
     * Set uo the auto complete searcher to find an address in the map
     */
    private void setUpAutoCompleteSearcher() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_maps));
        // Create a new Places Client instance
        PlacesClient placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        // Type of space the user will type in -> Addresses
        autocompleteSupportFragment.setTypeFilter(TypeFilter.REGIONS);
        // Favor results in this bounds (From Washington to Florida) -> Improve the prediction
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(28.932025, -81.582632),
                new LatLng(48.760309, -124.631258)
        ));
        autocompleteSupportFragment.setCountries("US", "MX");
        // Specify the type of place data to return
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(
                Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME));
        // When a place is selected
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull @NotNull Place place) {
                Log.i("MAP", place.getAddress());
                // Clean any other marker
                _mMap.clear();
                // Add a marker in the place and move the camera
                _mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(getResources().getString(R.string.place)));
                _mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng())); // Centers the map at the LatLng given
                // Set up the place added
                if(_place == null){
                    _place = new PlaceP();
                }
                _place.setAddress(place.getAddress());
                _place.setLatLng(place.getLatLng().toString());
                _place.setAddress(place.getName());
            }

            @Override
            public void onError(@NonNull @NotNull Status status) {
                Toast.makeText(MapsActivity.this, getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
            }
        });
        setUpDropDownMenu();
    }

    /**
     * Method to retrieve the different search filter types and populate the Dropdown Menu whit them.
     */
    private void populateDropdownMenu() {
        String[] typesFilter = getResources().getStringArray(R.array.filter_by);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, typesFilter);
        _binding.acFilterBy.setAdapter(adapter);
    }

    /***
     * Sets the onItemClickListener to save the value selected in a variable and use it to know how to filter
     */
    private void setUpDropDownMenu() {
        _binding.acFilterBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutocompleteSupportFragment autocompleteSupportFragment =
                        (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                if(position == 0){
                    autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
                }else if(position == 1){
                    autocompleteSupportFragment.setTypeFilter(TypeFilter.CITIES);
                }
                else if(position == 2){
                    autocompleteSupportFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
                }
                else if(position == 3){
                    autocompleteSupportFragment.setTypeFilter(TypeFilter.GEOCODE);
                }
                else if(position == 4){
                    autocompleteSupportFragment.setTypeFilter(TypeFilter.REGIONS);
                }
            }
        });
    }

}