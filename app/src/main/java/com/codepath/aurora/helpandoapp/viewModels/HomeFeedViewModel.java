package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.PlaceP;

public class HomeFeedViewModel extends ViewModel {
    private Contact _contact;
    private PlaceP _place;
    public static PlaceP publicPlace = null;

    public Contact getContact() {
        if (_contact == null) {
            _contact = new Contact();
        }
        return _contact;
    }

    public void setContact(Contact contact) {
        this._contact = contact;
    }

    public void cleanContact() {
        _contact = null;
    }

    public PlaceP getPlace() {
        if (_place == null) {
            _place = new PlaceP();
        }
        return _place;
    }

    public void setPlace(PlaceP place) {
        this._place = place;
    }

    public void cleanPlace() {
        _place = null;
        publicPlace = null;
    }

}