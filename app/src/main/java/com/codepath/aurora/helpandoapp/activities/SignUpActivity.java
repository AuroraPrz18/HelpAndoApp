package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivitySignUpBinding;
import com.codepath.aurora.helpandoapp.viewModels.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding _binding;
    private SignUpViewModel _viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        // Hide action bar in android
        getSupportActionBar().hide();
        // Getting an instance of the viewModel
        _viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDropdownMenu();
    }

    private void populateDropdownMenu() {
        String [] userTypes = getResources().getStringArray(R.array.user_type);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_user_type_item, userTypes);
        _binding.acUserType.setAdapter(adapter);
    }

    public void onClickSignUpButton(View view) {
        // TODO: Logic of the program. Sign up the user in the DB.
        // Is not needed to create again LoginActivity, only finalize this activity
        finish();
    }
}
