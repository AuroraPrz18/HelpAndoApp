package com.codepath.aurora.helpandoapp.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.adapters.PostAdapter;
import com.codepath.aurora.helpandoapp.databinding.HomeFeedFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

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
        _binding.ibDeleteContactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeContactCard(); // Shows DialogFragment
            }
        });
    }

    /**
     * Closes the ContactCard view and delete information related with this
     */
    private void closeContactCard() {
        _viewModel.cleanContact();
        _binding.cvContactCard.setVisibility(View.GONE);
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
        if(_viewModel.getContact()!=null){ // If this Post include a Contact, add this
            newPost.setContact(_viewModel.getContact());
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
                    return;
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.success_post), Toast.LENGTH_SHORT).show();
                // Clean the Contact Info
                closeContactCard();
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
}