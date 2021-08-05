package com.codepath.aurora.helpandoapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.adapters.PostAdapter;
import com.codepath.aurora.helpandoapp.databinding.UserProfileFragmentBinding;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.User;
import com.codepath.aurora.helpandoapp.viewModels.UserProfileViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {
    private UserProfileFragmentBinding _binding;
    private UserProfileViewModel _viewModel;
    private List<Post> _posts;
    private PostAdapter _adapter;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = UserProfileFragmentBinding.inflate(inflater, container, false);
        setUpRecyclerView();
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _binding.ibProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickProfilePhoto();
            }
        });
    }


    /**
     * Retrieve the month as a String
     *
     * @return
     */
    private String getMonth(int month) {
        String[] monthName = getResources().getStringArray(R.array.month_abrev);
        return monthName[month];
    }

    /**
     * Set initial text for all the views in the layout.
     */
    private void setUpProfile() {
        _viewModel.setUser(ParseUser.getCurrentUser());
        _binding.tvName.setText(_viewModel.getUser().getString(User.KEY_NAME));
        _binding.tvUsername.setText(_viewModel.getUser().getString(User.KEY_USERNAME));
        _binding.tvType.setText(_viewModel.getUser().getString(User.KEY_TYPE));
        _binding.tvPoints.setText(_viewModel.getUser().getNumber(User.KEY_POINTS) + "");
        if (_viewModel.getUser().getParseFile(User.KEY_PROFILE_PHOTO) != null) {
            Glide.with(getActivity()).load(_viewModel.getUser().getParseFile(User.KEY_PROFILE_PHOTO).getUrl())
                    .centerCrop()
                    .error(R.drawable.ic_user_24)
                    .placeholder(R.drawable.ic_user_24)
                    .into(_binding.ivPhotoUser);
        } else {
            _binding.ivPhotoUser.setImageDrawable(getActivity().getDrawable(R.drawable.ic_user_24));
        }
        ParseUser user = ParseUser.getCurrentUser();
        _binding.tvPoints.setText(user.getNumber(User.KEY_POINTS) + "");
        _binding.tvTaskC.setText(user.getNumber(User.KEY_TASKS_C) + "");
        _binding.tvTaskS.setText(user.getNumber(User.KEY_TASKS_S) + "");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        _viewModel.setUser(ParseUser.getCurrentUser());
        setUpProfile();
        setUpAllTheObservers();
    }

    private void setUpAllTheObservers() {
        _viewModel.getTasksCompleted().observe(getViewLifecycleOwner(), new Observer<List<ParseObject>>() {
            @Override
            public void onChanged(List<ParseObject> parseObjects) {
                // Update the chart
                _binding.chartProgressBar.setMaxValue(_viewModel.getMax());
                _binding.chartProgressBar.setDataList(_viewModel.getDataList());
                _binding.chartProgressBar.build();
                _binding.tvMaxPointsPerDay.setText(_viewModel.getMax() + "");
                _binding.tvWorkedDays.setText(_viewModel.getDataList().size() + "");
            }
        });
        _viewModel.getNewProfilePhoto().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNewProfilePhotoInDB) {
                if (isNewProfilePhotoInDB) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.success_profile_photo), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });
        _viewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                _posts.clear();
                _posts.addAll(posts);
                _adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Using ImagePicker library, you can choose a image from you gallery or take one with you camera.
     */
    public void onClickProfilePhoto() {
        ImagePicker.with(this)
                .crop()
                .compress(1024) // Final image size will be less than 1 MB
                .maxResultSize(1080, 1080) // Resolution should be less than 1080 x 1080
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) { // If everything went well
            // Get the Uri of the Image, use it instead of File to avoid storage permissions
            Uri uri = data.getData();
            _binding.ivPhotoUser.setImageURI(uri);
            File file = null;
            try {
                file = new File(new URI(uri.toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            _viewModel.saveProfilePhotoInDB(new ParseFile(file));
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
        }
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
}