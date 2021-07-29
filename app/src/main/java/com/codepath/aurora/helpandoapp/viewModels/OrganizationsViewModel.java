package com.codepath.aurora.helpandoapp.viewModels;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.aurora.helpandoapp.OrganizationsXmlParser;
import com.codepath.aurora.helpandoapp.models.Organization;
import com.codepath.aurora.helpandoapp.models.OrganizationsLastUpdate;
import com.codepath.aurora.helpandoapp.models.Theme;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Headers;

public class OrganizationsViewModel extends ViewModel {
    public static final String host = "https://api.globalgiving.org";
    public static final String organizations = "/api/public/orgservice/all/organizations/active/download";
    public static final String themes = "/api/public/projectservice/themes";

    public boolean fileIsEmpty;
    private long _downloadId;
    public File dir;
    private MutableLiveData<File> _file;
    private MutableLiveData<List<Organization>> _orgs;
    public String lastUpdateID;
    public boolean lastUpdateSaved;
    private MutableLiveData<Boolean> _doesItNeedUpdate;
    public String apiKey;

    public LiveData<Boolean> doesItNeedUpdate() {
        if (_doesItNeedUpdate == null) {
            _doesItNeedUpdate = new MutableLiveData<>();
            _doesItNeedUpdate.setValue(false);
        }
        return _doesItNeedUpdate;
    }


    public LiveData<File> getFile() {
        if (_file == null) {
            _file = new MutableLiveData<File>();
        }
        return _file;
    }

    public void setFile(File file) {
        if (_file == null) {
            _file = new MutableLiveData<File>();
        }
        this._file.setValue(file);
    }

    public LiveData<List<Organization>> getOrgs() {
        if (_orgs == null) {
            _orgs = new MutableLiveData<>();
        }
        return _orgs;
    }

    public void setOrgs(List<Organization> _orgs) {
        if (_orgs == null) {
            this._orgs = new MutableLiveData<>();
        }
        this._orgs.setValue(_orgs);
    }

    public long getDownloadId() {
        return _downloadId;
    }

    public String getURLOrganizations(String apiKey) {
        return host + organizations + "?api_key=" + apiKey;
    }

    public String getURLThemes(String apiKey) {
        return host + themes + "?api_key=" + apiKey;
    }

    /**
     * Executes a request asynchronously asking our API for non-profit organizations and fire the
     * onSuccess when the response returns a success code and onFailure if the response does not.
     * Using AsyncHttpClient library.
     */
    public void populateOrganizations(DownloadManager downloadManager) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("Accept", "application/json"); // To get the response in JSON format - XML is the default value
        String url = getURLOrganizations(apiKey);
        client.get(url, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Access a JSON array response with `json.jsonArray`
                try {
                    String urlToDownload = json.jsonObject.getJSONObject("download").getString("url");
                    // Download the XML File with all the nonprofits organizations
                    downloadFile(urlToDownload, downloadManager);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("Error", response);
            }
        });
    }

    public void downloadFile(String urlToDownload, DownloadManager downloadManager) {
        // Request a new download
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlToDownload));
        // Delete previous file if exists
        String pathFile = _file.getValue().toURI().toString();
        _file.getValue().delete();
        // Set the title of the download
        request.setTitle("nonprofits")
                .setDescription("Getting the information from the API. Please wait...")
                .setDestinationUri(Uri.parse(pathFile))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // Enqueue the download (saving its ID)
        _downloadId = downloadManager.enqueue(request);
    }

    /**
     * Convert the file downloaded into an array and saves it inside the ViewModel
     *
     * @param
     */
    public void onFileDownloaded() {
        boolean needUpdate = doesItNeedUpdate().getValue();
        if (lastUpdateSaved) {
            updateDateDownloaded(ParseUser.getCurrentUser());
        } else if ((!lastUpdateSaved && needUpdate) || fileIsEmpty) {
            createDateDownloaded(ParseUser.getCurrentUser());
        }
        new setUpFileDownloadedAsync().execute();
    }

    private void createDateDownloaded(ParseUser currentUser) {
        OrganizationsLastUpdate newObj = new OrganizationsLastUpdate();
        newObj.put("user", currentUser);
        // Saves the new object.
        newObj.saveInBackground();
    }

    public void updateDateDownloaded(ParseUser currentUser) {
        ParseQuery<OrganizationsLastUpdate> query = ParseQuery.getQuery("OrganizationsLastUpdate");
        // Retrieve the object by id
        query.getInBackground(lastUpdateID, (object, e) -> {
            if (e != null) {
                return;
            }
            Integer times = (Integer)object.getNumber("times")+(Integer)1;
            object.put("times", times);
            object.saveInBackground();
        });

    }

    /**
     * Look for the last update of the file, it should be one per day that the user opens the app
     *
     * @return
     */
    public void needUpdate() {
        ParseQuery<OrganizationsLastUpdate> lastUpdateQuery = ParseQuery.getQuery("OrganizationsLastUpdate");
        lastUpdateQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        lastUpdateQuery.orderByDescending("updatedAt");
        if (_doesItNeedUpdate == null) {
            _doesItNeedUpdate = new MutableLiveData<>();
        }
        _doesItNeedUpdate.setValue(false);
        lastUpdateQuery.findInBackground(new FindCallback<OrganizationsLastUpdate>() {
            @Override
            public void done(List<OrganizationsLastUpdate> updates, ParseException e) {
                if (e != null) return;
                if (updates.size() == 0) {
                    Log.d("Organizations", "New user/No information, download required");
                    lastUpdateSaved = false; // The backend don't have a row for this person
                    _doesItNeedUpdate.setValue(true); // Also when there is a file, it should download again because there is not a register for this user
                } else {
                    if (wasUpdatedToday(updates.get(0))) {
                        Log.d("Organizations", "NO need download");
                        _doesItNeedUpdate.setValue(false);
                    } else {
                        // TODO: DEBUG MORE
                        Log.d("Organizations", "Old File, download required");
                        lastUpdateID = updates.get(0).getObjectId();
                        lastUpdateSaved = true; // The backend has information about this user-downloads
                        _doesItNeedUpdate.setValue(lastUpdateSaved);
                    }
                }
            }
        });
    }

    /**
     * Retrieve all the themes from the API
     */
    public void getThemes() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("Accept", "application/json"); // To get the response in JSON format - XML is the default value
        String url = getURLThemes(apiKey);
        client.get(url, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONObject themes = json.jsonObject.getJSONObject("themes");
                    JSONArray themesArray = themes.getJSONArray("theme");
                    Theme.jsonArrayToObjects(themesArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("Error", response);
            }
        });
    }

    public boolean wasUpdatedToday(ParseObject update) {
        Date date = update.getUpdatedAt();
        Date today = new Date();
        Log.d("Organizations", "Today: "+ today.toString() + " - Last update: "+date.toString());
        if (date.getYear() != today.getYear()) return false;
        if (date.getMonth() != today.getMonth()) return false;
        if (date.getDay() != today.getDay()) return false;
        return true;
    }

    /**
     * Check if the Organizations file has information
     *
     * @return False if this user has been update this before, TRUE if there is not the file
     */
    public boolean isFileEmpty() {
        File file = new File(dir.getPath() + File.separator + "nonprofits");
        if (file.length() == 0) {
            Log.d("Organizations", "Download needed (file empty)");
            return true;
        }
        return false;
    }


    private class setUpFileDownloadedAsync extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            Log.d("orgs", _orgs.getValue().size() + " nonprofits downloaded");
            Log.d("orgs", _orgs.getValue().toString());
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                // Obtains input bytes from the file the app just downloaded in a file system
                FileInputStream fileInputStream = new FileInputStream(new File(_file.getValue().toURI()));
                try {
                    // Parse XML file into an array
                    OrganizationsXmlParser orgXmlParser = new OrganizationsXmlParser();
                    if (_orgs == null) getOrgs();
                    _orgs.postValue(orgXmlParser.parseXml(fileInputStream));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}