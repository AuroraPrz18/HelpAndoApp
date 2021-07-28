package com.codepath.aurora.helpandoapp.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.codepath.aurora.helpandoapp.LoginActivity;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityMainBinding;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private BroadcastReceiver _brDownload;
    private OrganizationsViewModel _viewModel;
    DownloadManager downloadManager;


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

        // Initialize the DownloadManager
        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        // Creates the Files where the information about nonprofits is going to be
        _viewModel.dir = new File(getExternalCacheDir(), "api");
        _viewModel.setFile(new File(_viewModel.dir.getPath() + File.separator + "nonprofits"));
        _brDownload = new downloadBroadcastReceiver();
        // To received information when this event happens
        registerReceiver(_brDownload, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        // TODO: Only make this call once a day
        _viewModel.populateOrganizations(getResources().getString(R.string.api_organizations), downloadManager);
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
     * Check if the file has been downloaded
     * @return
     */
    private class downloadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                long idDownload = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(_viewModel.getDownloadId() == idDownload){
                    _viewModel.onFileDownloaded(downloadManager);
                }
            }
        }
    }
}