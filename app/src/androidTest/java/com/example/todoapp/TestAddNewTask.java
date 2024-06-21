package com.example.todoapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.TestDatabaseHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Use AndroidJUnit4 for running tests on Android
@RunWith(AndroidJUnit4.class)
public class TestAddNewTask {

    private TestDatabaseHandler db; // Database handler for testing
    private FragmentScenario<AddNewTask> scenario; // Scenario for testing the fragment

    // Set up the test environment
    @Before
    public void setUp() {
        // Use in-memory database for testing
        db = new TestDatabaseHandler(ApplicationProvider.getApplicationContext());
        db.openDatabase();

        // Create the fragment with a TestDatabaseHandler
        scenario = FragmentScenario.launchInContainer(AddNewTask.class);
        scenario.onFragment(fragment -> fragment.setDatabaseHandler(db)); // Set the test database handler in the fragment
    }

    // Test adding a new task
    @Test
    public void testAddNewTask() {
        scenario.onFragment(fragment -> {
            EditText newTaskText = fragment.getView().findViewById(R.id.newTaskText); // Get the EditText for the new task
            Button newTaskSaveButton = fragment.getView().findViewById(R.id.newTaskButton); // Get the save button

            newTaskText.setText("New Task"); // Set the text for the new task
            newTaskSaveButton.performClick(); // Simulate a button click to save the task

            // Verify that the new task was added to the database
            assertTrue(db.getAllTasks().stream().anyMatch(task -> "New Task".equals(task.getTask())));
        });
    }

    // Test modifying an existing task
    @Test
    public void testModifyTask() {
        // Insert initial task
        ToDoModel initialTask = new ToDoModel();
        initialTask.setTask("Existing Task");
        db.insertTask(initialTask);

        // Get the inserted task's ID
        int taskId = db.getAllTasks().get(0).getId();

        // Create a bundle with the existing task's details
        Bundle bundle = new Bundle();
        bundle.putString("task", "Existing Task");
        bundle.putInt("id", taskId);

        // Set the bundle as arguments for the fragment
        scenario.onFragment(fragment -> fragment.setArguments(bundle));

        scenario.onFragment(fragment -> {
            EditText newTaskText = fragment.getView().findViewById(R.id.newTaskText); // Get the EditText for the task
            Button newTaskSaveButton = fragment.getView().findViewById(R.id.newTaskButton); // Get the save button

            newTaskText.setText("Modified Task"); // Modify the task text
            newTaskSaveButton.performClick(); // Simulate a button click to save the changes

            // Verify that the task was modified in the database
            assertTrue(db.getAllTasks().stream().anyMatch(task -> "Modified Task".equals(task.getTask())));
        });
    }

    // Test deleting a task
    @Test
    public void testDeleteTask() {
        // Insert initial task
        ToDoModel taskToDelete = new ToDoModel();
        taskToDelete.setTask("Task to Delete");
        db.insertTask(taskToDelete);

        // Get the inserted task's ID
        int taskId = db.getAllTasks().get(0).getId();

        // Delete the task
        db.deleteTask(taskId);

        // Verify that the task no longer exists in the database
        assertFalse(db.getAllTasks().stream().anyMatch(task -> taskId == task.getId()));
    }
}



