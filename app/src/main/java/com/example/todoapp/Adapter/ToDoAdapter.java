package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;

import java.util.List;

// Adapter class for managing the ToDo list in a RecyclerView
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList; // List of ToDo items
    private MainActivity activity; // Reference to the main activity
    private DatabaseHandler db; // Database handler for CRUD operations

    // Constructor for initializing the adapter with necessary dependencies
    public ToDoAdapter(DatabaseHandler db, MainActivity activity, List<ToDoModel> todoList){
        this.db = db;
        this.activity = activity;
        this.todoList = todoList;
    }

    // Method to create new ViewHolder instances
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task layout for individual items in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    // Method to bind data to the ViewHolder
    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase(); // Open the database
        ToDoModel item = todoList.get(position); // Get the item at the current position
        holder.task.setText(item.getTask()); // Set the task text
        holder.task.setChecked(toBoolean(item.getStatus())); // Set the checkbox status based on the item's status

        // Set a listener for checkbox state changes
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1); // Update status to completed in the database
                }
                else {
                    db.updateStatus(item.getId(), 0); // Update status to not completed in the database
                }
            }
        });
    }

    // Method to get the total count of items in the list
    public int getItemCount(){
        return todoList != null ? todoList.size() : 0;
    }

    // Helper method to convert an integer to a boolean
    private boolean toBoolean(int n){
        return n != 0;
    }

    // Method to set the tasks in the adapter and refresh the RecyclerView
    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // Method to get the context from the activity
    public Context getContext(){
        return activity;
    }

    // Method to delete an item from the list and notify the adapter
    public void deleteItem(int position){
        ToDoModel item = todoList.get(position); // Get the item at the specified position
        db.deleteTask(item.getId()); // Delete the item from the database
        todoList.remove(position); // Remove the item from the list
        notifyItemRemoved(position); // Notify the adapter that the item was removed
    }

    // Method to edit an item in the list
    public void editItem(int position){
        ToDoModel item = todoList.get(position); // Get the item at the specified position
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId()); // Add item ID to the bundle
        bundle.putString("task", item.getTask()); // Add task text to the bundle
        AddNewTask fragment = new AddNewTask(); // Create a new instance of AddNewTask fragment
        fragment.setArguments(bundle); // Set the arguments for the fragment
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG); // Show the fragment for editing
    }

    // ViewHolder class for holding and recycling views in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task; // Checkbox for the task

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox); // Initialize the checkbox
        }
    }
}


