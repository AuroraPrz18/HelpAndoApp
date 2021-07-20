package com.codepath.aurora.helpandoapp.fragments;

import android.os.Bundle;
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
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedFragment extends Fragment {
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
        _binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPostNow();
            }
        });
        setUpRecyclerView();
        populatePostsList();
    }

    public void onClickPostNow(){
        String postText = _binding.etPost.getText().toString();
        if(postText.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.text_required), Toast.LENGTH_SHORT).show();
            return;
        }
        _binding.btnPost.setEnabled(false);
        savePostInBackground(createNewPost());
    }

    /**
     * Creates a new Post with the information provided
     * @return
     */
    private Post createNewPost(){
        Post newPost = new Post();
        newPost.setAuthor(ParseUser.getCurrentUser());
        String postText = _binding.etPost.getText().toString();
        newPost.setText(postText);
        return newPost;
    }

    /**
     * Push a new Post to the backend server.
     * @return
     */
    private void savePostInBackground(Post newPost){
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                _binding.etPost.setText("");
                _binding.btnPost.setEnabled(true);
                if(e!=null){
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.success_post), Toast.LENGTH_SHORT).show();
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
}