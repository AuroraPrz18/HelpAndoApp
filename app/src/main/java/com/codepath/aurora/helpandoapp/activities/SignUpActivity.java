package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Hide action bar in android
        getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDropdownMenu();
    }

    private void populateDropdownMenu() {
        String [] userTypes = getResources().getStringArray(R.array.user_type);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_user_type_item, userTypes);
        binding.acUserType.setAdapter(adapter);
    }

    public void onClickSignUpButton(View view) {
        // TODO: Logic of the program. Sign up the user in the DB.
        // Is not needed to create again LoginActivity, only finalize this activity
        finish();
    }
}