package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ItemTaskBinding;
import com.codepath.aurora.helpandoapp.models.Task;

import org.jetbrains.annotations.NotNull;

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
     * @return
     */
    @Override
    public int getItemCount() {
        return _tasks.size();
    }

    /**
     * Class that provides a reference to the type of view used
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTaskBinding _binding;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            _binding = ItemTaskBinding.bind(itemView);
        }

        /**
         * Method to custom the data inside each item view
         * @param task
         */
        public void bind(Task task) {
            _binding.tvTitle.setText(task.getName());
            _binding.tvDescription.setText(task.getDescription());
            _binding.tvCategory.setText(_context.getResources().getString(R.string.category, task.getCategory()));
            _binding.tvPoints.setText(_context.getResources().getString(R.string.points, task.getPoints()+""));
        }
    }
}