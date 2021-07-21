package com.codepath.aurora.helpandoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.codepath.aurora.helpandoapp.LoginActivity;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityMainBinding;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
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
    public void logOut(){
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other activities from stack
            startActivity(intent);
        }else{
            Toast.makeText(this, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
        }
    }

}