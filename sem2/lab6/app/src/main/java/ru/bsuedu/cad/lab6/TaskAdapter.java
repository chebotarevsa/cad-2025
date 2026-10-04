package ru.bsuedu.cad.lab6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> tasks;

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());

        String status = task.isCompleted() ? "Выполнена" : "Не выполнена";
        String details = "Статус: " + status
                + "\nПриоритет: " + task.getPriority()
                + "\nКатегория: " + task.getCategory()
                + "\nСоздана: " + formatDate(task.getCreatedAt());
        holder.details.setText(details);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private String formatDate(String date) {
        return date == null ? "" : date.replace('T', ' ');
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView description;
        private final TextView details;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            description = itemView.findViewById(R.id.taskDescription);
            details = itemView.findViewById(R.id.taskDetails);
        }
    }
}
