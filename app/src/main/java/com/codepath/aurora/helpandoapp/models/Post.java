package com.codepath.aurora.helpandoapp.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Post")
public class Post extends ParseObject {
    public final static String KEY_AUTHOR = "author";
    public final static String KEY_TEXT_POST = "text";
    public final static String KEY_TASK = "task";
    public final static String KEY_IMAGE = "image";
    public final static String KEY_PLACE = "place";
    public final static String KEY_CONTACT_INFO = "contactInfo";

    public ParseObject getAuthor() {
        return getParseObject(KEY_AUTHOR);
    }

    public void setAuthor(ParseObject author) {
        put(KEY_AUTHOR, author);
    }

    public String getText() {
        return getString(KEY_TEXT_POST);
    }

    public void setText(String text) {
        put(KEY_TEXT_POST, text);
    }

    public ParseObject getTask() {
        return getParseObject(KEY_TASK);
    }

    public void setTask(ParseObject task) {
        put(KEY_TASK, task);
    }

    public ParseObject getContact() {
        return getParseObject(KEY_CONTACT_INFO);
    }

    public void setContact(ParseObject contact) {
        put(KEY_CONTACT_INFO, contact);
    }

    public ParseObject getPlace() {
        return getParseObject(KEY_PLACE);
    }

    public void setPlace(ParseObject place) {
        put(KEY_PLACE, place);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

}
