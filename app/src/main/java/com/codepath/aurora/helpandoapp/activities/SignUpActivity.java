package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.util.Log;
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
    private int _userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        // Hide action bar in android
        getSupportActionBar().hide();
        // Getting an instance of the viewModel
        _viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        // Setup the dropdown menu
        _userType = -1;
        _binding.acUserType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // _userType saves the last user type clicked
                _userType = position;
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
        String [] userTypes = getResources().getStringArray(R.array.user_type);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_user_type_item, userTypes);
        _binding.acUserType.setAdapter(adapter);
    }

    /**
     * Method called when the user click the SignUp button
     * @param view
     */
    public void onClickSignUpButton(View view) {
        createNewUser();
    }


    /**
     * Method that create a new ParseUser and then it try to sign it up.
     */
    private void createNewUser(){
        ParseUser newUser = new ParseUser();
        if(!isValid()){ // If all the information is completed
            Toast.makeText(this, "All the information is required", Toast.LENGTH_SHORT).show();
            return;
        }
        // Add all the information provided to the user
        newUser.setUsername(_binding.etUsername.getText().toString());
        newUser.put(User.KEY_NAME, _binding.etName.getText().toString());
        newUser.setPassword(_binding.etPassword.getText().toString());
        newUser.put(User.KEY_TYPE, getUserType());
        // Sign It up, if possible
        signUpAUser(newUser);
    }

    /**
     * Boolean method to check if the information provided by the user is completed. Returns TRUE if it is completed, FALSE if not.
     * @return
     */
    private boolean isValid() {
        if(_binding.etUsername.getText().toString().isEmpty()) return false;
        if(_binding.etName.getText().toString().isEmpty()) return false;
        if(_binding.etPassword.getText().toString().isEmpty()) return false;
        if(_userType == -1) return false;
        return true;
    }

    /**
     * Method that returns the last user type selected.
     * @return
     */
    private String getUserType(){
        String [] userTypes = getResources().getStringArray(R.array.user_type);
        return userTypes[_userType];
    }

    // TODO: CHANGE IT INSIDE THE VIEW MODEL
    /**
     * Asynchronous method to sign up a given user
     * @param newUser
     * @return
     */
    public boolean signUpAUser(ParseUser newUser){
        newUser.signUpInBackground(new SignUpCallback(){
            @Override
            public void done(ParseException e) {
                if(e != null){ // Something returns a exception
                    Toast.makeText(SignUpActivity.this, "Something went wrong "+e, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", "Something went wrong "+e);
                    return;
                }
                Toast.makeText(SignUpActivity.this, "You can log in now!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        return true;
    }
}
