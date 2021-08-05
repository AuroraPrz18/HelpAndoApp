package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.NewPostActivity;
import com.codepath.aurora.helpandoapp.activities.TaskDoneActivity;
import com.codepath.aurora.helpandoapp.databinding.ItemTaskBinding;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.Task;
import com.codepath.aurora.helpandoapp.models.User;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> _tasks;
    private Context _context;

    public TaskAdapter(List<Task> tasks, Context context) {
        this._tasks = tasks;
        this._context = context;
    }

    /**
     * Method called whenever the RecyclerView needs to create a new holder
     */
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Create and inflate a new view for an item
        View view = LayoutInflater.from(_context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Method that associates a ViewHolder with data.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Task task = _tasks.get(position);
        holder.bind(task);
    }

    /**
     * Method to get the size of the data
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return _tasks.size();
    }

    public void clear() {
        _tasks.clear();
    }

    /**
     * Class that provides a reference to the type of view used
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTaskBinding _binding;
        boolean _completed;
        private List<Post> _comments;
        private boolean _down;
        private PostAdapter _adapter;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            _binding = ItemTaskBinding.bind(itemView);
        }

        /**
         * Initializes the RecyclerView with a LayoutManager and with an Adapter
         */
        private void setUpRecyclerView(Task task) {
            _binding.rvComments.setLayoutManager(new LinearLayoutManager(_binding.getRoot().getContext()));
            _comments = new ArrayList<>();
            _adapter = new PostAdapter(_binding.getRoot().getContext(), _comments);
            _binding.rvComments.setAdapter(_adapter);
            populateComments(task);
        }

        /**
         * Retrieve the last 20 comments about this task
         */
        private void populateComments(Task task) {
            ParseQuery<Post> query = ParseQuery.getQuery("Post");
            query.orderByDescending("createdAt");
            query.whereEqualTo(Post.KEY_TASK, task); //
            query.include(Post.KEY_TASK);
            query.include(Post.KEY_AUTHOR);
            query.include(Post.KEY_PLACE);
            query.include(Post.KEY_CONTACT_INFO);
            query.setLimit(10);
            query.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> receivedPosts, ParseException e) {
                    if (e != null) {
                        _comments = new ArrayList<>();
                        return;
                    }
                    Log.d("comments", receivedPosts.toString());
                    _comments.clear();
                    _comments.addAll(receivedPosts);
                    _adapter.notifyDataSetChanged();
                }
            });
        }

        /**
         * Method to custom the data inside each item view
         *
         * @param task
         */
        public void bind(Task task) {
            _completed = false;
            _binding.tvTitle.setText(task.getName());
            _binding.tvDescription.setText(task.getDescription());
            _binding.tvCategory.setText(_context.getResources().getString(R.string.category, task.getCategory()));
            _binding.tvPoints.setText(_context.getResources().getString(R.string.points, task.getPoints() + ""));
            _binding.ibCheck.setVisibility(View.INVISIBLE);
            _binding.ibComment.setVisibility(View.INVISIBLE);
            // set a click listener to show the button to mark this task as a completed
            isCompleted(task);
            // Set click listener in the arrow
            _down = true;
            _binding.ibArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!_down) {
                        _comments.clear();
                        _binding.ibArrow.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_arrow_down));
                        _adapter.notifyDataSetChanged();
                        _down = true;
                    } else {
                        setUpRecyclerView(task);
                        _binding.ibArrow.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_arrow_up));
                        _down = false;
                    }
                }
            });
            _binding.ibArrow.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_arrow_down));
            if (_comments == null) {
                _comments = new ArrayList<>();
            }
            _comments.clear();
            if (_adapter != null)
                _adapter.notifyDataSetChanged();
            // Add when it was created
            Date date = task.getCreatedAt();
            String time = TimeAgo.using(date.getTime());
            if ((time.charAt(1) != ' ' || time.charAt(1) > 2) && time.contains("days ago")) {
                time = date.toString().substring(4, 10) + ", " + date.toString().substring(24);
            }
            _binding.tvTimestamp.setText(_context.getResources().getString(R.string.created) + time);
            // When the button btnDone is clicked it means that the user has completed this tasks
            _binding.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.newTaskCompleted(task); // Add this relation user-task into the TaskCompleted table
                    _binding.ibCheck.setVisibility(View.VISIBLE); // Show that it has been completed
                    _binding.ibComment.setVisibility(View.VISIBLE); // Show that it has been completed
                    _completed = true;
                    Intent intent = new Intent(_context, TaskDoneActivity.class);
                    _context.startActivity(intent);
                }
            });
            // When the button btnComment is clicked, it means that the user will be able to post something about that specific task
            _binding.ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Display the activity where the user can write the post with reference to this task
                    Intent intent = new Intent(_context, NewPostActivity.class);
                    intent.putExtra("Task", Parcels.wrap(task));
                    _context.startActivity(intent);
                    _comments.clear();
                    _binding.ibArrow.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_arrow_down));
                    _adapter.notifyDataSetChanged();
                    _down = true;
                }
            });
        }

        /**
         * Check if a given tasks has already been completed by the user.
         *
         * @param task
         */
        public void isCompleted(Task task) {
            // Set up the query to obtain how many times the user has completed this task
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TaskCompleted");
            query.whereEqualTo("user", ParseUser.getCurrentUser()); // Specify the user
            query.whereEqualTo("task", task); // Specify the task
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (count > 0) { // If the user completed the task at least once, set the check icon to be visible
                        _binding.ibCheck.setVisibility(View.VISIBLE);
                        animation();// Show that it has been completed
                        _completed = true;
                    }
                }
            });
        }

        private void animation(){
            Animation animation = AnimationUtils.loadAnimation(_context, R.anim.animation_scale);
            _binding.ibCheck.setVisibility(View.VISIBLE);
            _binding.ibCheck.startAnimation(animation);
            _binding.ibComment.setVisibility(View.VISIBLE);
            _binding.ibComment.startAnimation(animation);
        }
    }
}
