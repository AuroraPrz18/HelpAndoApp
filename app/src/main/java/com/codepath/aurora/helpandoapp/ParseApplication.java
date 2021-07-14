package com.codepath.aurora.helpandoapp;

import android.app.Application;

import com.codepath.aurora.helpandoapp.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 *  Class to setup Parse Initialize Parse using the server configuration
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Register User as a Parse Object
        ParseObject.registerSubclass(User.class);
        // Initialize Parse SDK when the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.applicationId))
                .clientKey(getString(R.string.clientKey))
                .server(getString(R.string.server))
                .build()
        );
    }
}
