package com.codepath.aurora.helpandoapp.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Parcel(analyze = PlaceP.class)
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


    /**
     * Return latitude and Longitude from an specific place
     *
     * @return
     */
    public static LatLng getLatLng(PlaceP place) {
        String latLng = place.getLatLng();
        latLng = latLng.substring(10, latLng.length() - 1);
        String[] latlongA = latLng.split(",");
        double latitude = Double.parseDouble(latlongA[0]);
        double longitude = Double.parseDouble(latlongA[1]);
        return new LatLng(latitude, longitude);
    }

    public static String getCountry(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String country = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address userAddress = addresses.get(0);
            country = userAddress.getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return country;
        }
    }

    public static String getCity(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String city = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address userAddress = addresses.get(0);
            city = userAddress.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return city;
        }
    }
}
