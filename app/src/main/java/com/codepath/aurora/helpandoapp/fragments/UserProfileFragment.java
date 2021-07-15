package com.codepath.aurora.helpandoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
        //_viewModel.setUser(ParseUser.getCurrentUser());
        setUpProfile();
    }

    private void setUpProfile() {
        // todo: check if it is updated when needed
        ParseUser user = ParseUser.getCurrentUser();
        _binding.tvName.setText(user.getString(User.KEY_NAME));
        _binding.tvUsername.setText(user.getString(User.KEY_USERNAME));
        _binding.tvType.setText(user.getString(User.KEY_TYPE));
        _binding.tvPoints.setText(user.getNumber(User.KEY_POINTS)+"");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}