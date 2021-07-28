package com.codepath.aurora.helpandoapp;

import android.app.Application;

import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.OrganizationsLastUpdate;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Class to setup Parse Initialize Parse using the server configuration
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Register User as a Parse Object
        ParseObject.registerSubclass(User.class);
        //Register Task as a Parse Object
        ParseObject.registerSubclass(Task.class);
        //Register Post as a Parse Object
        ParseObject.registerSubclass(Post.class);
        //Register Contact as a Parse Object
        ParseObject.registerSubclass(Contact.class);
        //Register Place as a Parse Object
        ParseObject.registerSubclass(PlaceP.class);
        //Register OrganizationsLastUpdate as a Parse Object
        ParseObject.registerSubclass(OrganizationsLastUpdate.class);
        // Initialize Parse SDK when the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.applicationId))
                .clientKey(getString(R.string.clientKey))
                .server(getString(R.string.server))
                .build()
        );
    }
}
