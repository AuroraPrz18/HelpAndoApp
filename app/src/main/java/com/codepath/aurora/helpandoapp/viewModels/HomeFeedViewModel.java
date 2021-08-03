package com.codepath.aurora.helpandoapp.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedViewModel extends ViewModel {
    private Contact _contact;
    private PlaceP _place;
    private ParseFile _image;
    public static PlaceP publicPlace = null;
    private MutableLiveData<Byte> _resultPost;
    public List<Post> posts = new ArrayList<>();
    public static boolean needUpdate1 = false;
    public static boolean needUpdate2 = false;

    public LiveData<Byte> getResultPost() {
        if (_resultPost == null) {
            _resultPost = new MutableLiveData<>();
            _resultPost.setValue((byte) 0);
        }
        return _resultPost;
    }

    public void setResultPost(Byte resultPost) {
        if (_resultPost == null) {
            _resultPost = new MutableLiveData<>();
            _resultPost.setValue((byte) 0);
        }
        _resultPost.setValue(resultPost);
    }

    private MutableLiveData<Byte> _resultPostSaved;

    public LiveData<Byte> getResultPostSaved() {
        if (_resultPostSaved == null) {
            _resultPostSaved = new MutableLiveData<>();
            _resultPostSaved.setValue((byte) 0);
        }
        return _resultPostSaved;
    }

    public void setResultPostSaved(Byte resultPostSaved) {
        if (_resultPostSaved == null) {
            _resultPostSaved = new MutableLiveData<>();
            _resultPostSaved.setValue((byte) 0);
        }
        _resultPostSaved.setValue(resultPostSaved);
    }

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

    public ParseFile getImage() {
        return _image;
    }

    public void setImage(ParseFile image) {
        this._image = image;
    }

    public void cleanImage() {
        _image = null;
    }

    /**
     * Push a new Post to the backend server.
     *
     * @return
     */
    public void savePostInBackground(Post newPost) {
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    setResultPostSaved((byte) -1);
                    Log.e("ERROR", e.toString());
                    return;
                }
                setResultPostSaved((byte) 1);
                populatePostsList();
            }
        });
    }

    /**
     * Populates Posts list retrieving them from de backend server
     */
    public void populatePostsList() {
        ParseQuery<Post> query = ParseQuery.getQuery("Post");
        query.orderByDescending("createdAt");
        query.include(Post.KEY_TASK);
        query.include(Post.KEY_AUTHOR);
        query.include(Post.KEY_PLACE);
        query.include(Post.KEY_CONTACT_INFO);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> receivedPosts, ParseException e) {
                if (e != null) {
                    setResultPost((byte) -1);
                    Log.e("ERROR", e.toString());
                    return;
                }
                setResultPost((byte) 1);
                // Save received posts
                posts.clear();
                posts.addAll(receivedPosts);
            }
        });
    }
}