package com.codepath.aurora.helpandoapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.MapsActivity;
import com.codepath.aurora.helpandoapp.activities.NewPostActivity;
import com.codepath.aurora.helpandoapp.adapters.PostAdapter;
import com.codepath.aurora.helpandoapp.databinding.HomeFeedFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.User;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

// This class implements ContactDialogListener to define what happens with the information provided by the
// ContactDialog fragment when is need
public class HomeFeedFragment extends Fragment implements ContactDialog.ContactDialogListener {
    private HomeFeedFragmentBinding _binding;
    private HomeFeedViewModel _viewModel;
    private PostAdapter _adapter;


    public static HomeFeedFragment newInstance() {
        return new HomeFeedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = HomeFeedFragmentBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(HomeFeedViewModel.class);
        setOnClickListeners();
        setUpRecyclerView();
        _viewModel.populatePostsList();
        setUpProfilePhoto();
        setUpListeners();
    }

    /**
     * Listeners of the ViewModel and LiveData
     */
    private void setUpListeners() {
        _viewModel.getResultPostSaved().observe(getViewLifecycleOwner(), new Observer<Byte>() {
            @Override
            public void onChanged(Byte result) {
                if (result != 0) {
                    _binding.etPost.setText("");
                    _binding.btnPost.setEnabled(true);
                }
                if (result == -1) { //Something went wrong
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    _viewModel.setResultPostSaved((byte) 0);
                } else if (result == 1) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.success_post), Toast.LENGTH_SHORT).show();
                    // Clean the Contact Info
                    closeContactCard();
                    // Clean the Place Card
                    closePlaceCard();
                    // Clean the Image Card
                    closeImageCard();
                    _viewModel.setResultPostSaved((byte) 0);
                }
            }
        });
        _viewModel.getResultPost().observe(getViewLifecycleOwner(), new Observer<Byte>() {
            @Override
            public void onChanged(Byte result) {
                if (result == -1) { //Something went wrong
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    _viewModel.setResultPost((byte) 0);
                } else if (result == 1) {
                    // Notify the adapter of data change
                    _adapter.notifyDataSetChanged();
                    _viewModel.setResultPost((byte) 0);
                }
            }
        });
    }

    /**
     * Show the profile photo of the current user if possible
     */
    private void setUpProfilePhoto() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user.getParseFile(User.KEY_PROFILE_PHOTO) != null) {
            Glide.with(getActivity()).load(user.getParseFile(User.KEY_PROFILE_PHOTO).getUrl())
                    .centerCrop()
                    .error(R.drawable.ic_user_24)
                    .placeholder(R.drawable.ic_user_24)
                    .into(_binding.ivPhotoUser);
        } else {
            _binding.ivPhotoUser.setImageDrawable(getActivity().getDrawable(R.drawable.ic_user_24));
        }
    }

    /**
     * Sets all the onClickListeners for this Activity
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickListeners() {
        _binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPostNow();
            }
        });
        _binding.ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactDialog(); // Shows DialogFragment
            }
        });
        _binding.ibPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Request permission
                }
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                getActivity().startActivityForResult(intent, 100);
            }
        });
        _binding.ibDeleteContactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeContactCard();
            }
        });
        _binding.ibClosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePlaceCard();
            }
        });
        _binding.ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAnImage();
            }
        });
        _binding.lytMakeAPost.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // Open the New Post section in its own Activity
                    Intent intent = new Intent(getActivity(), NewPostActivity.class);
                    //intent.putExtra("Post", Parcels.wrap(createNewPost()));
                    // todo: send the information already written
                    startActivity(intent);
                    return super.onDoubleTap(e);
                }
            });

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    /**
     * Using ImagePicker library, you can choose a image from you gallery or take one with you camera.
     */
    private void chooseAnImage() {
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
    private void openContactDialog() {
        ContactDialog dialog = new ContactDialog();
        // Adding the Contact info as an Argument
        Bundle bundle = new Bundle();
        bundle.putParcelable("Contact", Parcels.wrap(_viewModel.getContact()));
        dialog.setArguments(bundle);
        dialog.setTargetFragment(HomeFeedFragment.this, 1);
        dialog.show(getParentFragmentManager(), "Dialog tag");
    }

    /**
     * When the Post button is clicked, the program will try to save the information on the backend
     */
    public void onClickPostNow() {
        String postText = _binding.etPost.getText().toString(); // Gets the info
        if (postText.isEmpty()) { // Validates the information
            Toast.makeText(getActivity(), getResources().getString(R.string.text_required), Toast.LENGTH_SHORT).show();
            return;
        }
        _binding.btnPost.setEnabled(false); // Disables the button to avoid some post with the same information while the process happens
        savePostInBackground(createNewPost()); // Saves the Post if possible
    }

    private void savePostInBackground(Post newPost) {
        _viewModel.setResultPost((byte) 0); // Initialize the result
        _viewModel.savePostInBackground(newPost);
    }

    /**
     * Creates a new Post with the information provided
     *
     * @return
     */
    private Post createNewPost() {
        Post newPost = new Post();
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
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvPosts.setLayoutManager(new LinearLayoutManager(_binding.getRoot().getContext()));
        _adapter = new PostAdapter(_binding.getRoot().getContext(), _viewModel.posts);
        _binding.rvPosts.setAdapter(_adapter);
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
        if (HomeFeedViewModel.publicPlace != null) {
            setPlaceInfo(HomeFeedViewModel.publicPlace);
        }
        if (HomeFeedViewModel.needUpdate1) {
            _viewModel.populatePostsList();
            HomeFeedViewModel.needUpdate1 = false;
        }
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
        if (resultCode == Activity.RESULT_OK) { // If everything went well
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
            Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
        }
    }

}