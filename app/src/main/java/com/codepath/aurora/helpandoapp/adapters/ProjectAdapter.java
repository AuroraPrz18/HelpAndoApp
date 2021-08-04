package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.models.Project;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    Context _context;
    List<Project> _projects;

    public ProjectAdapter(Context _context, List<Project> _projects) {
        this._context = _context;
        this._projects = _projects;
    }

    /**
     * Method called whenever the RecyclerView needs to create a new holder
     */
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Create and inflate a new view for an item
        View view = LayoutInflater.from(_context).inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Method that associates a ViewHolder with data.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Project project = _projects.get(position);
        holder.bind(project);
    }

    /**
     * Method to get the size of the data
     * @return
     */
    @Override
    public int getItemCount() {
        return _projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        /**
         * Method to custom the data inside each item view and populate all their views
         * @param project
         */
        public void bind(Project project) {
        }
    }
}
