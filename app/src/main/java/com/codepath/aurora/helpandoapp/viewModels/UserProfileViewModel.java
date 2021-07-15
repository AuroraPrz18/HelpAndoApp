package com.codepath.aurora.helpandoapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.parse.ParseUser;

public class UserProfileViewModel extends ViewModel {
    private ParseUser _user;

    public ParseUser getUser() {
        return _user;
    }

    public void setUser(ParseUser user) {
        this._user = user;
    }
}