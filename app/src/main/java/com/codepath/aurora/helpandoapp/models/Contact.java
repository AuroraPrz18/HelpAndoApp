package com.codepath.aurora.helpandoapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Contact")
public class Contact extends ParseObject {
    public final static String KEY_NAME = "name";
    public final static String KEY_TELEPHONE = "telephone";
    public final static String KEY_EXTRA_INFO = "text";

    public Contact() {
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getTelephone() {
        return getString(KEY_TELEPHONE);
    }

    public void setTelephone(String telephone) {
        put(KEY_TELEPHONE, telephone);
    }

    public String getExtraInfo() {
        return getString(KEY_EXTRA_INFO);
    }

    public void setExtraInfo(String extraInfo) {
        put(KEY_EXTRA_INFO, extraInfo);
    }
}
