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

public class NewPostActivity extends AppCompatActivity {
    ActivityNewPostBinding _binding;
    Task task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        _binding.tvTask.setVisibility(View.GONE);
        if(getIntent()!=null){
            task = (Task) Parcels.unwrap(getIntent().getParcelableExtra("Task"));
            _binding.tvTask.setText(getResources().getString(R.string.postAboutTask, task.getName()));
            _binding.tvTask.setVisibility(View.VISIBLE);
        }
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
            Toast.makeText(this, getResources().getString(R.string.text_required), Toast.LENGTH_SHORT).show();
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
        if(task!=null){
            newPost.setTask(task);
        }
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
                    Toast.makeText(NewPostActivity.this, getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(NewPostActivity.this, getResources().getString(R.string.success_post), Toast.LENGTH_SHORT).show();
            }
        });
    }
}