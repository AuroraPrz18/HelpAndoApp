package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivitySignUpBinding;
import com.codepath.aurora.helpandoapp.models.User;
import com.codepath.aurora.helpandoapp.viewModels.SignUpViewModel;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
        // Creates a ViewModel the first time the system calls an activity's onCreate() method
        // On the other hand, if the activity is re-created it receives the same viewModel than the first Activity
        _viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        // Setup the dropdown menu
        setUpDropDownMenu();
    }

    /***
     * Sets the onItemClickListener to save the value selected in a variable and use it to know what
     * option was selected
     */
    private void setUpDropDownMenu() {
        _binding.acUserType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Volunteer
                    _binding.tfEmail.setVisibility(View.VISIBLE);
                    _binding.tfPhone.setVisibility(View.VISIBLE);
                } else {
                    _binding.tfEmail.setVisibility(View.GONE);
                    _binding.tfPhone.setVisibility(View.GONE);
                }
                // _userType saves the last user type clicked
                _viewModel.setTypeUser(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDropdownMenu();
    }

    /**
     * Method to retrieve the different user types and populate the Dropdown Menu whit them.
     */
    private void populateDropdownMenu() {
        String[] userTypes = getResources().getStringArray(R.array.user_type);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_user_type_item, userTypes);
        _binding.acUserType.setAdapter(adapter);
    }

    /**
     * Method called when the user click the SignUp button
     *
     * @param view
     */
    public void onClickSignUpButton(View view) {
        if (isValid()) { // If all the data provided is complete
            signUpAUserInBackground(createNewUser());
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.fields_required), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Method that create a new ParseUser with the information provided
     */
    private ParseUser createNewUser() {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(_binding.etUsername.getText().toString());
        newUser.put(User.KEY_NAME, _binding.etName.getText().toString());
        newUser.setPassword(_binding.etPassword.getText().toString());
        newUser.put(User.KEY_TYPE, getUserType());
        if (!_binding.etEmail.getText().toString().isEmpty()) {
            newUser.put(User.KEY_EMAIL, _binding.etEmail.getText().toString());
        }
        if (!_binding.etPhone.getText().toString().isEmpty()) {
            newUser.put(User.KEY_PHONE, _binding.etPhone.getText().toString());
        }
        return newUser;
    }

    /**
     * Boolean method to check if the information provided by the user is completed. Returns TRUE if it is completed, FALSE if not.
     *
     * @return
     */
    private boolean isValid() {
        if (_binding.etUsername.getText().toString().isEmpty()) return false;
        if (_binding.etName.getText().toString().isEmpty()) return false;
        if (_binding.etPassword.getText().toString().isEmpty()) return false;
        if (_viewModel.getTypeUser() == -1) return false;
        return true;
    }

    /**
     * Method that returns the last user type selected.
     *
     * @return
     */
    private String getUserType() {
        String[] userTypes = getResources().getStringArray(R.array.user_type);
        return userTypes[_viewModel.getTypeUser()];
    }

    // TODO: CHANGE IT INSIDE THE VIEW MODEL

    /**
     * Asynchronous method to sign up a given user
     *
     * @param newUser
     * @return
     */
    public boolean signUpAUserInBackground(ParseUser newUser) {
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) { // Something returns a exception
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.success_sign_up), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        return true;
    }
}
