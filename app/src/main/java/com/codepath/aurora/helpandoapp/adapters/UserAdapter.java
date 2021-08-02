package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ItemUserBinding;
import com.codepath.aurora.helpandoapp.fragments.ContactDialogInPost;
import com.codepath.aurora.helpandoapp.models.User;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context _context;
    List<ParseUser> _users;

    public UserAdapter(Context _context, List<ParseUser> _users) {
        this._context = _context;
        this._users = _users;
    }

    /**
     * Method called whenever the RecyclerView needs to create a new holder
     */
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Create and inflate a new view for an item
        View view = LayoutInflater.from(_context).inflate(R.layout.item_user, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    /**
     * Method that associates a ViewHolder with data.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ParseUser user = _users.get(position);
        holder.bind(user);
    }

    /**
     * Method to get the size of the data
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return _users.size();
    }

    /**
     * Class that provides a reference to the type of view used
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding _binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            _binding = ItemUserBinding.bind(itemView);
            if (isCurrentUserASponsor()) {
                // Only Sponsors can see email and phone from the volunteers to get in touch with them
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openContactDialog(_users.get(getAdapterPosition()));
                    }
                });
            }
        }

        public void bind(ParseUser user) {
            //_binding.ivPhotoUser.setImageDrawable(R.drawable.ic_user_24);
            _binding.tvName.setText(user.getString(User.KEY_NAME));
            _binding.tvUsername.setText(user.getString(User.KEY_USERNAME));
            _binding.tvType.setText("Volunteer");
            _binding.tvPoints.setText(user.getNumber(User.KEY_POINTS) + "");
            _binding.tvTaskC.setText(user.getNumber(User.KEY_TASKS_C) + "");
            _binding.tvTaskS.setText(user.getNumber(User.KEY_TASKS_S) + "");
            _binding.ivAnimation.setVisibility(View.GONE);
            setUpProfilePhoto(user);
            if (isCurrentUserASponsor()) _binding.ivAnimation.setVisibility(View.VISIBLE);
        }

        /**
         * Show the profile photo of the user if possible
         */
        private void setUpProfilePhoto(ParseUser user) {
            if (user.getParseFile(User.KEY_PROFILE_PHOTO) != null) {
                Glide.with(_context).load(user.getParseFile(User.KEY_PROFILE_PHOTO).getUrl())
                        .centerCrop()
                        .error(R.drawable.ic_user_24)
                        .placeholder(R.drawable.ic_user_24)
                        .into(_binding.ivPhotoUser);
            } else {
                _binding.ivPhotoUser.setImageDrawable(_context.getDrawable(R.drawable.ic_user_24_orange));
            }
        }

        /**
         * Shows the ContactDialogInPost with the contact information attached to this user
         */
        private void openContactDialog(ParseUser user) {
            ContactDialogInPost dialog = new ContactDialogInPost();
            // Adding the Contact info as an Argument
            Bundle bundle = new Bundle();
            bundle.putParcelable("Volunteer", Parcels.wrap(user));
            dialog.setArguments(bundle);
            dialog.show(((FragmentActivity) _context).getSupportFragmentManager(), "Contact Info Show");
        }
    }

    private boolean isCurrentUserASponsor() {
        return ParseUser.getCurrentUser().getString(User.KEY_TYPE).equals("Sponsor");
    }
}
