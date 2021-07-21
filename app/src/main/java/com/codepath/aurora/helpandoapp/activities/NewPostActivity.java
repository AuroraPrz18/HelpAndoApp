package com.codepath.aurora.helpandoapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ActivityNewPostBinding;
import com.codepath.aurora.helpandoapp.models.Post;
import com.codepath.aurora.helpandoapp.models.Task;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

/**
 * This Activity is needed when a user want to post something about an specific Task that tha user's already done
 */
public class NewPostActivity extends AppCompatActivity {
    ActivityNewPostBinding _binding;
    Task task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding with the view
        _binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        setTaskInfoVisible();
        _binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPostNow();
            }
        });
    }

    /**
     * Sets information about a Task inside the UI when needed, to specify that the user is writing about that Task
     */
    private void setTaskInfoVisible() {
        // The information about the task is going to be visible only when the Activity has received
        // a task sent by the previous Activity with all the information about it.
        _binding.tvTask.setVisibility(View.GONE);
        if (getIntent() != null) { // If it receives a Task object
            task = (Task) Parcels.unwrap(getIntent().getParcelableExtra("Task"));
            _binding.tvTask.setText(getResources().getString(R.string.postAboutTask, task.getName())); // Sets the text of the TextView
            _binding.tvTask.setVisibility(View.VISIBLE); // Makes the TextView visible
        }
    }

    /**
     * When the Post button is clicked, the program will try to save the information on the backend
     */
    public void onClickPostNow() {
        String postText = _binding.etPost.getText().toString(); // Gets the info
        if (postText.isEmpty()) { // Validates the information
            Toast.makeText(this, getResources().getString(R.string.text_required), Toast.LENGTH_SHORT).show();
            return;
        }
        _binding.btnPost.setEnabled(false); // Disables the button to avoid some post with the same information while the process happens
        savePostInBackground(createNewPost()); // Saves the Post if possible
    }

    /**
     * Creates a new Post with the information provided
     *
     * @return
     */
    private Post createNewPost() {
        Post newPost = new Post();
        if (task != null) { // If this Post is a comment to a Task, specify to what Task
            newPost.setTask(task);
        }
        newPost.setAuthor(ParseUser.getCurrentUser());
        String postText = _binding.etPost.getText().toString();
        newPost.setText(postText);
        return newPost;
    }

    /**
     * Push a new Post to the backend server.
     *
     * @return
     */
    private void savePostInBackground(Post newPost) {
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                _binding.etPost.setText("");
                _binding.btnPost.setEnabled(true);
                if (e != null) {
                    Toast.makeText(NewPostActivity.this, getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(NewPostActivity.this, getResources().getString(R.string.success_post), Toast.LENGTH_SHORT).show();
            }
        });
    }
}