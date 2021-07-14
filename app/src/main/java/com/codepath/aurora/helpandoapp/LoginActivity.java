package com.codepath.aurora.helpandoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.aurora.helpandoapp.activities.MainActivity;
import com.codepath.aurora.helpandoapp.activities.SignUpActivity;
import com.codepath.aurora.helpandoapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Hide action bar in android
        getSupportActionBar().hide();

    }

    public void onClickSignInButton(View view) {
        // TODO: Logic of the program. Check if is a valid user, etc.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}