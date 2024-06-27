package com.example.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestSearchTask {
    private DatabaseHandler db;
    private SQLiteDatabase sqLiteDatabase;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        sqLiteDatabase = SQLiteDatabase.create(null); // Create an in-memory database
        db = new DatabaseHandler(context, sqLiteDatabase);
        db.openDatabase();
    }

    @After
    public void tearDown() {
        sqLiteDatabase.close();
    }

    @Test
    public void testSearchTasks() {
        // Insert tasks into the database
        ToDoModel task1 = new ToDoModel();
        task1.setTask("Buy groceries");
        db.insertTask(task1);

        ToDoModel task2 = new ToDoModel();
        task2.setTask("Read a book");
        db.insertTask(task2);

        ToDoModel task3 = new ToDoModel();
        task3.setTask("Buy milk");
        db.insertTask(task3);

        // Search for tasks containing the keyword "Buy"
        List<ToDoModel> searchResults = db.searchTasks("Buy");

        // Verify the search results
        assertEquals(2, searchResults.size());
        assertTrue(searchResults.stream().anyMatch(task -> task.getTask().equals("Buy groceries")));
        assertTrue(searchResults.stream().anyMatch(task -> task.getTask().equals("Buy milk")));

        // Search for tasks containing the keyword "Read"
        searchResults = db.searchTasks("Read");

        // Verify the search results
        assertEquals(1, searchResults.size());
        assertTrue(searchResults.stream().anyMatch(task -> task.getTask().equals("Read a book")));

        // Search for tasks containing the keyword "Nonexistent"
        searchResults = db.searchTasks("Nonexistent");

        assertEquals(0, searchResults.size());
    }
}