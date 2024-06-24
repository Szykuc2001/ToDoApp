package com.example.todoapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Main activity class for managing the ToDo list
public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksRecyclerView; // RecyclerView for displaying tasks
    private ToDoAdapter tasksAdapter; // Adapter for managing tasks in the RecyclerView
    private List<ToDoModel> taskList; // List of tasks
    private DatabaseHandler db; // Database handler for CRUD operations
    private FloatingActionButton fab; // Floating action button for adding new tasks
    private EditText searchEditText;
    private Button searchButton;
    private Button statsButton;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity

        db = new DatabaseHandler(this); // Initialize the database handler
        db.openDatabase(); // Open the database

        taskList = new ArrayList<>(); // Initialize the task list

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView); // Initialize the RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set the layout manager for the RecyclerView

        // Pass the database handler and task list to the adapter
        tasksAdapter = new ToDoAdapter(db, MainActivity.this, taskList);
        tasksRecyclerView.setAdapter(tasksAdapter); // Set the adapter for the RecyclerView

        fab = findViewById(R.id.fab); // Initialize the floating action button
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        statsButton = findViewById(R.id.statsButton);

        // Set up item touch helper for swipe actions
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView); // Attach item touch helper to the RecyclerView

        // Retrieve all tasks from the database and reverse the list
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList); // Set the tasks in the adapter

        // Set a click listener for the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the AddNewTask dialog when the button is clicked
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchEditText.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    taskList = db.searchTasks(keyword);
                    Collections.reverse(taskList);
                    tasksAdapter.setTasks(taskList);
                    tasksAdapter.notifyDataSetChanged();
                } else {
                    taskList = db.getAllTasks();
                    Collections.reverse(taskList);
                    tasksAdapter.setTasks(taskList);
                    tasksAdapter.notifyDataSetChanged();
                }
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsActivity.newInstance(MainActivity.this,StatsActivity.class);
            }
        });
    }

    // Handle the dialog close event to refresh the task list
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks(); // Retrieve all tasks from the database
        Collections.reverse(taskList); // Reverse the task list
        tasksAdapter.setTasks(taskList); // Set the tasks in the adapter
        tasksAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
}
