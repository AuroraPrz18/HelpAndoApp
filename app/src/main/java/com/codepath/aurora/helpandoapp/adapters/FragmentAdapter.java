package com.codepath.aurora.helpandoapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.codepath.aurora.helpandoapp.fragments.HomeFeedFragment;
import com.codepath.aurora.helpandoapp.fragments.OrganizationsFragment;
import com.codepath.aurora.helpandoapp.fragments.SearchFragment;
import com.codepath.aurora.helpandoapp.fragments.ToDoFragment;
import com.codepath.aurora.helpandoapp.fragments.UserProfileFragment;

import org.jetbrains.annotations.NotNull;

/**
 * Class that works as an adapter for the viewPager2
 */
public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0) return  new HomeFeedFragment();
        else if(position==1) return  new ToDoFragment();
        else if(position==2) return  new SearchFragment();
        else if(position==3) return  new OrganizationsFragment();
        else if(position==4) return  new UserProfileFragment();
        else return new HomeFeedFragment();
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
