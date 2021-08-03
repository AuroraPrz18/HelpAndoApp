package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.MapsActivityInPost;
import com.codepath.aurora.helpandoapp.databinding.ItemPostBinding;
import com.codepath.aurora.helpandoapp.fragments.ContactDialogInPost;
import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.PlaceP;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.models.User;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context _context;
    List<Post> _posts;

    public PostAdapter(Context context, List<Post> posts) {
        this._context = context;
        this._posts = posts;
    }

    /**
     * Method called whenever the RecyclerView needs to create a new holder
     */
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Create and inflate a new view for an item
        View view = LayoutInflater.from(_context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Method that associates a ViewHolder with data.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Post post = _posts.get(position);
        holder.bind(post);
    }

    /**
     * Method to get the size of the data
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return _posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding _binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            _binding = ItemPostBinding.bind(itemView);
        }

        /**
         * Method to custom the data inside each item view
         *
         * @param post
         */
        public void bind(Post post) {
            _binding.tvUser.setText(post.getAuthor().getString(User.KEY_NAME));
            _binding.tvText.setText(post.getText());
            // Add when it was published
            Date date = post.getCreatedAt();
            String time = TimeAgo.using(date.getTime());
            if((time.charAt(1)!=' ' ||  time.charAt(1)>2) && time.contains("days ago")){
                time = date.toString().substring(4, 10) + ", " + date.toString().substring(24);
            }
            _binding.tvTimestamp.setText(time);
            // if it is a Post about some Task, show Task's information
            if (post.getTask() != null) {
                _binding.tvTask.setVisibility(View.VISIBLE);
                _binding.tvTask.setText(_context.getResources().getString(R.string.answerTask, ((Task) post.getTask()).getName()));
            } else {
                _binding.tvTask.setVisibility(View.GONE);
            }
            // if this post contain ContactCard, show Contact information icon
            if (post.getContact() != null) {
                _binding.ibShowContact.setVisibility(View.VISIBLE);
                // If the icon is clicked, show all the information about how to help
                _binding.ibShowContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openContactDialog((Contact) post.getContact());
                    }
                });
            } else {
                _binding.ibShowContact.setVisibility(View.GONE);
            }
            // if this post contains PlaceCard, show Place information card
            if (post.getPlace() != null) {
                _binding.cvPlace.setVisibility(View.VISIBLE);
                _binding.tvAddress.setText(((PlaceP) post.getPlace()).getAddress());
                // If the icon is clicked, show a map with this information
                _binding.cvPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPlace((PlaceP) post.getPlace());
                    }
                });
            } else {
                _binding.cvPlace.setVisibility(View.GONE);
            }
            // if this post contains ImageCard, show it
            if (post.getImage() != null) {
                _binding.cvImage.setVisibility(View.VISIBLE);
                Glide.with(_context).load(post.getImage().getUrl()).into(_binding.ivImagePost);
            } else {
                _binding.cvImage.setVisibility(View.GONE);
            }
            // Show the profile photo if possible
            if (post.getAuthor() != null && post.getAuthor().getParseFile(User.KEY_PROFILE_PHOTO) != null) {
                Glide.with(_context).load(post.getAuthor().getParseFile(User.KEY_PROFILE_PHOTO).getUrl())
                        .centerCrop()
                        .error(R.drawable.ic_user_24)
                        .placeholder(R.drawable.ic_user_24)
                        .into(_binding.ivPhotoUser);
            } else {
                _binding.ivPhotoUser.setImageDrawable(_context.getDrawable(R.drawable.ic_user_24));
            }
        }

        /**
         * Shows MapsActivity with the place information attached to this post
         */
        private void openPlace(PlaceP place) {
            Intent intent = new Intent(_context, MapsActivityInPost.class);
            intent.putExtra("Place", Parcels.wrap(place));
            _context.startActivity(intent);
        }

        /**
         * Shows the ContactDialogInPost with the contact information attached to this post
         */
        private void openContactDialog(Contact contact) {
            ContactDialogInPost dialog = new ContactDialogInPost();
            // Adding the Contact info as an Argument
            Bundle bundle = new Bundle();
            bundle.putParcelable("Contact", Parcels.wrap(contact));
            dialog.setArguments(bundle);
            dialog.show(((FragmentActivity) _context).getSupportFragmentManager(), "Contact Show");
        }
    }
}
