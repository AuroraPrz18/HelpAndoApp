package com.codepath.aurora.helpandoapp.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.aurora.helpandoapp.LoginActivity;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityMainBinding;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private BroadcastReceiver _brDownload;
    private long _downloadId;
    private OrganizationsViewModel _viewModel;
    DownloadManager downloadManager;
    File dir;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Define a navigation controller, which is the object that manages app navigation within a NavHost
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        // set up the bottom navigation view
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        // Creates a ViewModel the first time the system calls an activity's onCreate() method
        // On the other hand, if the activity is re-created it receives the same viewModel than the first Activity
        _viewModel = new ViewModelProvider(this).get(OrganizationsViewModel.class);


        dir = new File(getExternalCacheDir(), "api");
        file = new File(dir.getPath() + File.separator + "nonprofits");
        _brDownload = new downloadBroadcastReceiver();
        // To received information when this event happens
        registerReceiver(_brDownload, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        populateOrganizations();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // To inflate the new items in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_top_menu, menu);
        MenuItem logOutItem = menu.findItem(R.id.imLogout);
        // Set the listener to the log out item
        logOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logOut();
                return true;
            }
        });
        return true;
    }

    /**
     * Logs out the current user and finishes the activity
     */
    public void logOut() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other activities from stack
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            PlaceP place = (PlaceP) Parcels.unwrap(data.getParcelableExtra("Place"));
            HomeFeedViewModel.publicPlace = place;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(_brDownload);
    }

    /**
     * Executes a request asynchronously asking our API for non-profit organizations and fire the
     * onSuccess when the response returns a success code and onFailure if the response does not.
     * Using AsyncHttpClient library.
     */
    private void populateOrganizations() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("Accept", "application/json"); // To get the response in JSON format - XML is the default value
        String url = _viewModel.getURLOrganizations(getResources().getString(R.string.api_organizations));
        Log.d("DEBUG ARRAY", url);
        client.get(url, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Access a JSON array response with `json.jsonArray`
                try {
                    String urlToDownload = json.jsonObject.getJSONObject("download").getString("url");
                    Log.d("DEBUG ARRAY", "->"+urlToDownload);
                    // Download the XML File with all the nonprofits organizations
                    downloadFile(urlToDownload);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DEBUG ARRAY", response);
            }
        });
    }

    public void downloadFile(String urlToDownload) {
        // Request a new download
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlToDownload));
        // Set the title of the download
        request.setTitle("nonprofits")
                .setDescription("Getting the information from the API. Please wait...")
                .setDestinationUri(Uri.parse(file.toURI().toString()))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // Initialize the DownloadManager
        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        // Enqueue the download (saving its ID)
        _downloadId = downloadManager.enqueue(request);
    }

    /**
     * Check if the file has been downloaded
     * @return
     */
    private class downloadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                long idDownload = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(_downloadId == idDownload){
                    try {
                        // TODO: SHOULD BE ASYNC
                        //-------- Read the file as a String ------------------
                        // Obtains input bytes from the file the app just downloaded in a file system
                        FileInputStream fileInputStream = new FileInputStream(new File(file.toURI()));
                        try {
                            // Save the unique FileChannel object associated with the file downloaded
                            FileChannel fileChannel = fileInputStream.getChannel();
                            // Maps all the content of this channel's file directly into memory.
                            // In this way, we get a direct byte buffer whose content is a memory-mapped region of a file.
                            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
                             // Decode byte sequences into a sequence of characters and convert it in a String
                            String data = Charset.defaultCharset().decode(mappedByteBuffer).toString();
                            //xmlToArray(data);
                            Log.e("DATA", data.length()+"");
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
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}