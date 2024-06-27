// StatsActivityTest.java

package com.example.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StatsActivityTest {

    private DatabaseHandler db;
    private SQLiteDatabase sqLiteDatabase;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        sqLiteDatabase = SQLiteDatabase.create(null); // Create an in-memory database
        db = new DatabaseHandler(context, sqLiteDatabase);
        db.openDatabase();

        // Insert tasks into the database
        ToDoModel task1 = new ToDoModel();
        task1.setTask("Task 1");
        db.insertTask(task1);

        ToDoModel task2 = new ToDoModel();
        task2.setTask("Task 2");
        db.insertTask(task2);

        ToDoModel task3 = new ToDoModel();
        task3.setTask("Task 3");
        task3.setStatus(1); // Completed task
        db.insertTask(task3);
    }

    @After
    public void tearDown() {
        sqLiteDatabase.close();
    }

    @Test
    public void testStatsDisplayedCorrectly() {
        ActivityScenario<StatsActivity> scenario = ActivityScenario.launch(StatsActivity.class);
        scenario.onActivity(activity -> {
            // Inject the in-memory DatabaseHandler
            activity.setDatabaseHandler(db);
        });

        scenario.onActivity(activity -> {
            // Verify the stats are displayed correctly
            TextView totalTasksTextView = activity.findViewById(R.id.totalTasksTextView);
            TextView completedTasksTextView = activity.findViewById(R.id.completedTasksTextView);
            TextView pendingTasksTextView = activity.findViewById(R.id.pendingTasksTextView);

            assertEquals("Total Tasks: 3", totalTasksTextView.getText().toString());
            assertEquals("Completed Tasks: 1", completedTasksTextView.getText().toString());
            assertEquals("Pending Tasks: 2", pendingTasksTextView.getText().toString());
        });
    }

    @Test
    public void testBackButton() {
        ActivityScenario<StatsActivity> scenario = ActivityScenario.launch(StatsActivity.class);
        scenario.onActivity(activity -> {
            // Inject the in-memory DatabaseHandler
            activity.setDatabaseHandler(db);
        });

        scenario.onActivity(activity -> {
            // Click the back button
            activity.findViewById(R.id.backButton).performClick();

            // Verify the activity is finished
            assertEquals(true, activity.isFinishing());
        });
    }
}
