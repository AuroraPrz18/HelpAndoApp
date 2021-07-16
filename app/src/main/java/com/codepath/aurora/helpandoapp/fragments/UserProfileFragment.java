package com.codepath.aurora.helpandoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.codepath.aurora.helpandoapp.databinding.UserProfileFragmentBinding;
import com.codepath.aurora.helpandoapp.models.User;
import com.codepath.aurora.helpandoapp.viewModels.UserProfileViewModel;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class UserProfileFragment extends Fragment {
    private UserProfileFragmentBinding _binding;
    private UserProfileViewModel _viewModel;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _binding = UserProfileFragmentBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Set initial text for all the views in the layout.
     */
    private void setUpProfile() {
        _binding.tvName.setText(_viewModel.getUser().getString(User.KEY_NAME));
        _binding.tvUsername.setText(_viewModel.getUser().getString(User.KEY_USERNAME));
        _binding.tvType.setText(_viewModel.getUser().getString(User.KEY_TYPE));
        _binding.tvPoints.setText(_viewModel.getUser().getNumber(User.KEY_POINTS) + "");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        _viewModel.setUser(ParseUser.getCurrentUser());
        setUpProfile();
        //setUpAllTheObservers();
    }

    private void setUpAllTheObservers() {
        _viewModel.getPoints().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                _binding.tvPoints.setText(String.valueOf(integer));
            }
        });
    }

}