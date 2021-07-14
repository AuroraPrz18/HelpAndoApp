package com.codepath.aurora.helpandoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.codepath.aurora.helpandoapp.activities.MainActivity;
import com.codepath.aurora.helpandoapp.activities.SignUpActivity;
import com.codepath.aurora.helpandoapp.databinding.ActivityLoginBinding;
import com.codepath.aurora.helpandoapp.viewModels.LogInViewModel;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding _binding;
    private LogInViewModel _viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        // Hide action bar in android
        getSupportActionBar().hide();
        // Getting an instance of the viewModel
        _viewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        // If there is an active session - allow user persistence
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Method called when the user click the LogIn button
     * @param view
     */
    public void onClickSignInButton(View view) {
        loginUser(_binding.etUsername.getText().toString(), _binding.etPassword.getText().toString());
    }

    /**
     * Method that tries to log in the user with given credentials.
     * @param username
     * @param password
     */
    private void loginUser(String username, String password) {
        // TODO: CHANGE IT TO THE MODEL VIEW
        ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e != null){
                            Toast.makeText(LoginActivity.this, "Forgotten credentials? Try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    /**
     * Method called when the user click the SignUp button
     * @param view
     */
    public void onClickSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}