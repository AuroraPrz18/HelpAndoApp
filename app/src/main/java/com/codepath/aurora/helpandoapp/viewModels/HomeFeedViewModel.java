package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.codepath.aurora.helpandoapp.models.Contact;

public class HomeFeedViewModel extends ViewModel {
    private Contact _contact;

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

}