package com.codepath.aurora.helpandoapp.viewModels;

import android.app.DownloadManager;
import android.net.Uri;
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

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class OrganizationsViewModel extends ViewModel {
    private long _downloadId;
    public File dir;
    private MutableLiveData<File> _file;

    public List<Organization> orgs = new ArrayList<>();
    public static final String host = "https://api.globalgiving.org";
    //public static final String organizations = "/api/public/orgservice/all/organizations/vetted";
    public static final String organizations = "/api/public/orgservice/all/organizations/vetted/download";

    public LiveData<File> getFile() {
        if(_file==null){
            _file = new MutableLiveData<File>();
        }
        return _file;
    }

    public void setFile(File file) {
        if(_file==null){
            _file = new MutableLiveData<File>();
        }
        this._file.setValue(file);
    }

    public long getDownloadId() {
        return _downloadId;
    }

    public String getURLOrganizations(String apiKey) {
        return host + organizations + "?api_key=" + apiKey;
    }

    /**
     * Executes a request asynchronously asking our API for non-profit organizations and fire the
     * onSuccess when the response returns a success code and onFailure if the response does not.
     * Using AsyncHttpClient library.
     */
    public void populateOrganizations(String urlHost, DownloadManager downloadManager) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("Accept", "application/json"); // To get the response in JSON format - XML is the default value
        String url = getURLOrganizations(urlHost);
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

    public void downloadFile(String urlToDownload,DownloadManager downloadManager) {
        // Request a new download
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlToDownload));
        // Set the title of the download
        request.setTitle("nonprofits")
                .setDescription("Getting the information from the API. Please wait...")
                .setDestinationUri(Uri.parse(_file.getValue().toURI().toString()))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // Enqueue the download (saving its ID)
        _downloadId = downloadManager.enqueue(request);
    }

    public void onFileDownloaded(DownloadManager downloadManager) {
        try {
            // Obtains input bytes from the file the app just downloaded in a file system
            FileInputStream fileInputStream = new FileInputStream(new File(_file.getValue().toURI()));
            try {
                // Parse XML file into an array
                OrganizationsXmlParser orgXmlParser = new OrganizationsXmlParser();
                List<Organization> orgs = orgXmlParser.parseXml(fileInputStream);
                Log.e("orgs", orgs.size()+"");
                Log.e("orgs", orgs.toString()+"");
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // Remove downloaded file from cache
                downloadManager.remove(_downloadId);
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
    }
}