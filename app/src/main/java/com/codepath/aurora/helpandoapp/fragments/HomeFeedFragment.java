package com.codepath.aurora.helpandoapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.MapsActivity;
import com.codepath.aurora.helpandoapp.adapters.PostAdapter;
import com.codepath.aurora.helpandoapp.databinding.HomeFeedFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

// This class implements ContactDialogListener to define what happens with the information provided by the
// ContactDialog fragment when is need
public class HomeFeedFragment extends Fragment implements ContactDialog.ContactDialogListener {
    private HomeFeedFragmentBinding _binding;
    private HomeFeedViewModel _viewModel;
    private PostAdapter _adapter;
    private List<Post> _posts;

    public static HomeFeedFragment newInstance() {
        return new HomeFeedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = HomeFeedFragmentBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(HomeFeedViewModel.class);
        setOnClickListeners();
        setUpRecyclerView();
        populatePostsList();
    }

    /**
     * Sets all the onClickListeners for this Activity
     */
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", e.toString());
                    return;
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.success_post), Toast.LENGTH_SHORT).show();
                // Clean the Contact Info
                closeContactCard();
                // Clean the Place Card
                closePlaceCard();
                // Clean the Image Card
                closeImageCard();
            }
        });
    }


    /**
     * Initializes the RecyclerView with a LayoutManager and with an Adapter
     */
    private void setUpRecyclerView() {
        _binding.rvPosts.setLayoutManager(new LinearLayoutManager(_binding.getRoot().getContext()));
        _posts = new ArrayList<>();
        _adapter = new PostAdapter(_binding.getRoot().getContext(), _posts);
        _binding.rvPosts.setAdapter(_adapter);
    }

    /**
     * Populates Posts list retrieving them from de backend server
     */
    private void populatePostsList() {
        ParseQuery<Post> query = ParseQuery.getQuery("Post");
        query.orderByDescending("createdAt");
        query.include(Post.KEY_TASK);
        query.include(Post.KEY_AUTHOR);
        query.include(Post.KEY_PLACE);
        query.include(Post.KEY_CONTACT_INFO);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> receivedPosts, ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Save received posts
                _posts.clear();
                _posts.addAll(receivedPosts);
                // Notify the adapter of data change
                _adapter.notifyDataSetChanged();
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
        if (HomeFeedViewModel.publicPlace != null) {
            setPlaceInfo(HomeFeedViewModel.publicPlace);
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