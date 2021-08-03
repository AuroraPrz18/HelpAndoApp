package com.codepath.aurora.helpandoapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityNewPostBinding;
import com.codepath.aurora.helpandoapp.fragments.ContactDialog;
import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.models.User;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.codepath.aurora.helpandoapp.viewModels.OrganizationsViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This Activity is needed when a user want to post something about an specific Task that tha user's already done
 */
public class NewPostActivity extends AppCompatActivity implements ContactDialog.ContactDialogListener {
    private ActivityNewPostBinding _binding;
    private Task _task = null;
    private Post _post = null;
    private HomeFeedViewModel _viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding with the view
        _binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        _viewModel = new ViewModelProvider(this).get(HomeFeedViewModel.class);
        setContentView(_binding.getRoot());
        if (getIntent() != null) {
            if (getIntent().getExtras().containsKey("Post")) { // If it was sent from HomeFeedFragment
                _post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("Post"));
                setInfoPost();
            } else if (getIntent().getExtras().containsKey("Task")) { // If it was sent from HomeFeedFragment
                _task = (Task) Parcels.unwrap(getIntent().getParcelableExtra("Task"));
                setTaskInfoVisible();
            }
        }
        _binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPostNow();
            }
        });
        _binding.ibPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Request permission
                }
                Intent intent = new Intent(NewPostActivity.this, MapsActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        setUpProfilePhoto();
    }


    /**
     * Show the profile photo of the current user if possible
     */
    private void setUpProfilePhoto() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user.getParseFile(User.KEY_PROFILE_PHOTO) != null) {
            Glide.with(this).load(user.getParseFile(User.KEY_PROFILE_PHOTO).getUrl())
                    .centerCrop()
                    .error(R.drawable.ic_user_24)
                    .placeholder(R.drawable.ic_user_24)
                    .into(_binding.ivPhotoUser);
        } else {
            _binding.ivPhotoUser.setImageDrawable(this.getDrawable(R.drawable.ic_user_24));
        }
    }

    /**
     * Sets the information that the user wrote before open this Activity
     */
    private void setInfoPost() {
        // TODO: RETRIEVE THE INFO
    }

    /**
     * Sets information about a Task inside the UI when needed, to specify that the user is writing about that Task
     */
    private void setTaskInfoVisible() {
        // The information about the task is going to be visible only when the Activity has received
        // a task sent by the previous Activity with all the information about it.
        _binding.tvTask.setVisibility(View.GONE);
        // If it receives a Task object
        _task = (Task) Parcels.unwrap(getIntent().getParcelableExtra("Task"));
        _binding.tvTask.setText(getResources().getString(R.string.postAboutTask, _task.getName())); // Sets the text of the TextView
        _binding.tvTask.setVisibility(View.VISIBLE); // Makes the TextView visible
    }

    /**
     * Using ImagePicker library, you can choose a image from you gallery or take one with you camera.
     */
    public void chooseAnImage(View view) {
        ImagePicker.with(this)
                .crop()
                .compress(1024) // Final image size will be less than 1 MB
                .maxResultSize(1080, 1080) // Resolution should be less than 1080 x 1080
                .start();
    }

    /**
     * Closes the ContactCard view and delete information related with this
     */
    private void closeContactCard() {
        _viewModel.cleanContact();
        _binding.cvContactCard.setVisibility(View.GONE);
    }

    /**
     * Closes the PlaceCard view and delete information related with this
     */
    private void closePlaceCard() {
        _viewModel.cleanPlace();
        _binding.cvPlace.setVisibility(View.GONE);
    }

    /**
     * Closes the PlaceCard view and delete information related with this
     */
    private void closeImageCard() {
        _viewModel.cleanImage();
        _binding.cvImage.setVisibility(View.GONE);
    }

    /**
     * Shows the ContactDialogFragment to fill a form with information about contact information to help
     */
    public void openContactDialog(View view) {
        ContactDialog dialog = new ContactDialog();
        // Adding the Contact info as an Argument
        Bundle bundle = new Bundle();
        bundle.putParcelable("Contact", Parcels.wrap(_viewModel.getContact()));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Dialog tag");
    }

    /**
     * When the Post button is clicked, the program will try to save the information on the backend
     */
    public void onClickPostNow() {
        String postText = _binding.etPost.getText().toString(); // Gets the info
        if (postText.isEmpty()) { // Validates the information
            Toast.makeText(this, getResources().getString(R.string.text_required), Toast.LENGTH_SHORT).show();
            return;
        }
        _binding.btnPost.setEnabled(false); // Disables the button to avoid some post with the same information while the process happens
        savePostInBackground(createNewPost()); // Saves the Post if possible
    }

    /**
     * Creates a new Post with the information provided
     *
     * @return
     */
    private Post createNewPost() {
        Post newPost = new Post();
        if (_task != null) { // If this Post is a comment to a Task, specify to what Task
            newPost.setTask(_task);
        }
        if (_viewModel.getContact().getName() != null) { // If this Post include a Contact, add this
            newPost.setContact(_viewModel.getContact());
        }
        if (_viewModel.getPlace().getAddress() != null) { // If this Post include a Location, add this
            newPost.setPlace(_viewModel.getPlace());
        }
        if (_viewModel.getImage() != null) { // If this Post include an iMAGE, add this
            newPost.setImage(_viewModel.getImage());
        }
        newPost.setAuthor(ParseUser.getCurrentUser());
        String postText = _binding.etPost.getText().toString();
        newPost.setText(postText);
        return newPost;
    }

    /**
     * Push a new Post to the backend server.
     *
     * @return
     */
    private void savePostInBackground(Post newPost) {
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                _binding.etPost.setText("");
                _binding.btnPost.setEnabled(true);
                if (e != null) {
                    Toast.makeText(NewPostActivity.this, getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", e.toString());
                    return;
                }
                Toast.makeText(NewPostActivity.this, getResources().getString(R.string.success_post), Toast.LENGTH_SHORT).show();
                // Clean the Contact Info
                closeContactCard();
                // Clean the Place Card
                closePlaceCard();
                // Clean the Image Card
                closeImageCard();
                HomeFeedViewModel.needUpdate1 = true;
                finish();
            }
        });
    }

    /**
     * Method called when the ContactDialog Fragment has a Positive answer and a new Contact has been created.
     *
     * @param contact
     */
    @Override
    public void saveInformation(Contact contact) {
        _binding.cvContactCard.setVisibility(View.VISIBLE);
        _binding.tvNameContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etNameContact_hint) + "</b> "
                + contact.getName()));
        _binding.tvPhoneContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etTelContact_hint) + "</b> "
                + contact.getTelephone()));
        _binding.tvExtraInfoContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etExtraInfoContact_hint) + "</b> "
                + contact.getExtraInfo()));
        _viewModel.setContact(contact);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Display the place info in the Post Section
     *
     * @param place
     */
    public void setPlaceInfo(PlaceP place) {
        _viewModel.setPlace(place);
        _binding.cvPlace.setVisibility(View.VISIBLE);
        _binding.tvAddress.setText(_viewModel.getPlace().getAddress());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            PlaceP place = (PlaceP) Parcels.unwrap(data.getParcelableExtra("Place"));
            if (data.getBooleanExtra("IsUserLocation", false)) {
                User.userLocation = PlaceP.getLatLng(place);
                User.getCity(this);
                User.getCountry(this);
                OrganizationsViewModel.setUserUpdate(true);
            } else {
                setPlaceInfo(place);
            }
        } else if (resultCode == Activity.RESULT_OK) { // If everything went well
            // Get the Uri of the Image, use it instead of File to avoid storage permissions
            Uri uri = data.getData();
            _binding.ivImagePost.setImageURI(uri);
            File file = null;
            try {
                file = new File(new URI(uri.toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            // Change the post's image
            _viewModel.setImage(new ParseFile(file));
            _binding.cvImage.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
        }
    }
}