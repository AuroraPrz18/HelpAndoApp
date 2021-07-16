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

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.HomeFeedFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.viewModels.HomeFeedViewModel;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class HomeFeedFragment extends Fragment {
    private HomeFeedFragmentBinding _binding;
    private HomeFeedViewModel _viewModel;

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

}