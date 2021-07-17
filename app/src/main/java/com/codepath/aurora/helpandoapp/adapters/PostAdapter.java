package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ItemPostBinding;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context _context;
    List<Post> _posts;

    public PostAdapter(Context _context, List<Post> _posts) {
        this._context = _context;
        this._posts = _posts;
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
            if(post.getTask()!=null){
                _binding.tvTask.setVisibility(View.VISIBLE);
                _binding.tvTask.setText(_context.getResources().getString(R.string.answerTask, ((Task)post.getTask()).getName()));
            }else {
                _binding.tvTask.setVisibility(View.GONE);
            }
            _binding.tvText.setText(post.getText());
        }
    }


}
