package com.codepath.aurora.helpandoapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Place")
public class PlaceP extends ParseObject {
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LATLNG = "latlng";
    private static final String KEY_NAME = "name";

    // Required empty constructor
    public PlaceP() {
    }

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
    }

    public String getLatLng() {
        return getString(KEY_LATLNG);
    }

    public void setLatLng(String latlng) {
        put(KEY_LATLNG, latlng);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }
}
