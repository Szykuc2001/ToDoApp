package com.example.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.example.todoapp.Utils.TestDatabaseHandler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

// Use AndroidJUnit4 for running tests on Android
@RunWith(AndroidJUnit4.class)
public class TestRecyclerItemTouchHelper {

    private ToDoAdapter adapter; // Adapter for managing tasks
    private RecyclerView recyclerView; // RecyclerView for displaying tasks
    private DatabaseHandler db; // Database handler for CRUD operations
    private MainActivity activity; // Reference to the main activity

    // Rule for managing the activity lifecycle during testing
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    // Set up the test environment
    @Before
    public void setUp() {
        // Initialize the DatabaseHandler with in-memory database
        Context context = ApplicationProvider.getApplicationContext();
        db = new TestDatabaseHandler(context);
        db.openDatabase();

        // Get the MainActivity instance
        activity = activityRule.getActivity();

        // Initialize the adapter with the MainActivity instance, the database handler, and a non-null task list
        List<ToDoModel> taskList = new ArrayList<>();

        // Add a task to the list
        ToDoModel task = new ToDoModel();
        task.setId(1);
        task.setTask("Test Task");
        task.setStatus(0);
        taskList.add(task);
        db.insertTask(task);  // Insert the task into the database to maintain consistency

        adapter = new ToDoAdapter(db, activity, taskList);

        // Get the RecyclerView from the activity and set the adapter
        activityRule.getActivity().runOnUiThread(() -> {
            recyclerView = activity.findViewById(R.id.tasksRecyclerView);
            recyclerView.setAdapter(adapter);
        });
    }

    // Test swiping an item to edit
    @Test
    public void testSwipeToEdit() {
        activityRule.getActivity().runOnUiThread(() -> {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0); // Get the ViewHolder for the first item
            if (viewHolder != null) {
                RecyclerItemTouchHelper itemTouchHelper = new RecyclerItemTouchHelper(adapter);
                itemTouchHelper.onSwiped(viewHolder, ItemTouchHelper.RIGHT); // Simulate a right swipe
                verify(adapter).editItem(0); // Verify that the editItem method is called
            }
        });
    }

    // Test swiping an item to delete
    @Test
    public void testSwipeToDelete() {
        activityRule.getActivity().runOnUiThread(() -> {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0); // Get the ViewHolder for the first item
            if (viewHolder != null) {
                RecyclerItemTouchHelper itemTouchHelper = new RecyclerItemTouchHelper(adapter);
                itemTouchHelper.onSwiped(viewHolder, ItemTouchHelper.LEFT); // Simulate a left swipe

                // Create an AlertDialog for confirming deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Confirm", (dialog, which) -> {
                    verify(adapter).deleteItem(0); // Verify that the deleteItem method is called
                });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    verify(adapter).notifyItemChanged(0); // Verify that the item is restored if deletion is canceled
                });

                AlertDialog dialog = builder.create();
                dialog.show(); // Show the dialog
            }
        });
    }
}

