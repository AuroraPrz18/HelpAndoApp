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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.codepath.aurora.helpandoapp.LoginActivity;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.adapters.FragmentAdapter;
import com.codepath.aurora.helpandoapp.databinding.ActivityMainBinding;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.models.User;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    // Number of fragment inside it
    private static final int _NUM_FRAGMENT = 5;
    private ViewPager2 _vPager;
    private FragmentStateAdapter _pAdapter; // To provide the pages to the view pager
    ActivityMainBinding binding;
    private BroadcastReceiver _brDownload;
    private OrganizationsViewModel _viewModel;
    DownloadManager downloadManager;
    ViewPager2.OnPageChangeCallback onPageChangeCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OrganizationsViewModel.apiKey = getResources().getString(R.string.api_organizations);
        // Define a navigation controller, which is the object that manages app navigation within a NavHost
                //NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.pager);
                //NavController navController = navHostFragment.getNavController();
        // set up the bottom navigation view
                //NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        _vPager = binding.pager;
        _pAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        _vPager.setAdapter(_pAdapter);

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if(item.getItemId() == R.id.homeFeedFragment){
                    _vPager.setCurrentItem(0);
                }else if(item.getItemId() == R.id.toDoFragment){
                    _vPager.setCurrentItem(1);
                }
                else if(item.getItemId() == R.id.searchFragment){
                    _vPager.setCurrentItem(2);
                }
                else if(item.getItemId() == R.id.organizationsFragment){
                    _vPager.setCurrentItem(3);
                }
                else if(item.getItemId() == R.id.userProfileFragment){
                    _vPager.setCurrentItem(4);
                }
                _pAdapter.notifyDataSetChanged();
                return true;
            }
        });
        onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        };
        binding.pager.registerOnPageChangeCallback(onPageChangeCallback);


        // Creates a ViewModel the first time the system calls an activity's onCreate() method
        // On the other hand, if the activity is re-created it receives the same viewModel than the first Activity
        _viewModel = new ViewModelProvider(this).get(OrganizationsViewModel.class);
        _viewModel.apiKey = getResources().getString(R.string.api_organizations);
        // Initialize the DownloadManager
        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        // Creates the Files where the information about nonprofits is going to be
        _viewModel.dir = new File(getExternalCacheDir(), "api");
        _viewModel.setFile(new File(_viewModel.dir.getPath() + File.separator + "nonprofits"));
        _brDownload = new downloadBroadcastReceiver();
        // Get the themes
        _viewModel.getThemes();
        // To received information when this event happens
        registerReceiver(_brDownload, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        // Only download the Organizations File once a day if the user has it file in its cache
        if (_viewModel.isFileEmpty()) { // If it needs to download it
            _viewModel.fileIsEmpty = true;
            _viewModel.populateOrganizations(downloadManager);
        } else { // If it doesn't need to read it, just convert to XML
            _viewModel.fileIsEmpty = false;
            _viewModel.onFileDownloaded();
            // To listen if an update is needed based in its last update
            setUpAllTheObservers();
            _viewModel.needUpdate(); // It will change the value of doesItNeedUpdate to true or false
        }

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
            if(data.getBooleanExtra("IsUserLocation", false)){
                User.userLocation = PlaceP.getLatLng(place);
                User.getCity(this);
                User.getCountry(this);
                OrganizationsViewModel.setUserUpdate(true);
            }else{
                HomeFeedViewModel.publicPlace = place;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(_brDownload);
        binding.pager.unregisterOnPageChangeCallback(onPageChangeCallback);
    }

    /**
     * Check if the file has been downloaded
     *
     * @return
     */
    private class downloadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                long idDownload = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (_viewModel.getDownloadId() == idDownload) {
                    _viewModel.onFileDownloaded();
                }
            }
        }
    }

    /**
     * Set Up all the observers to listen the changes inside the View Model
     */
    private void setUpAllTheObservers() {
        // This also going to listen when the user create a new Task to update the UI in the moment
        _viewModel.doesItNeedUpdate().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean doesItNeedUpdate) {
                if (doesItNeedUpdate == true) { // The file is one day or more old, it has to be updated
                    _viewModel.populateOrganizations(downloadManager);
                }
            }
        });
    }

    /**
     * Method to start receiving location updates
     */
    /*    public void  getLastLocation(){
        int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            // Create location services client
            FusedLocationProviderClient locationUser = LocationServices.getFusedLocationProviderClient(this);
            // Get the last known location
            Task location = locationUser.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location == null){

                    }
                }
            });
        }

    }*/

    @Override
    public void onBackPressed() {
        if(_vPager.getCurrentItem()==0)
            super.onBackPressed(); // If we don't have any other previous step
        else
            _vPager.setCurrentItem(_vPager.getCurrentItem() - 1); // Return to previous step
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}