package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

// Database handler class for managing CRUD operations on the ToDo list
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1; // Database version
    private static final String NAME = "toDoListDatabase"; // Database name
    private static final String TODO_TABLE = "todo"; // Table name
    private static final String ID = "id"; // Column name for ID
    private static final String TASK = "task"; // Column name for task description
    private static final String STATUS = "status"; // Column name for task status
    // SQL statement to create the table
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db; // Database instance

    // Constructor for creating the database handler
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    // New constructor for in-memory database
    public DatabaseHandler(Context context, SQLiteDatabase db) {
        super(context, NAME, null, VERSION);
        this.db = db;
        if (!isTableExists(db, TODO_TABLE)) {
            onCreate(db); // Create the table if it doesn't exist
        }
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE); // Execute the SQL statement to create the table
    }

    // Check if a table exists in the database
    boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    // Open the database connection
    public void openDatabase() {
        if (db == null || !db.isOpen()) {
            db = this.getWritableDatabase(); // Get writable database
            if (!isTableExists(db, TODO_TABLE)) {
                onCreate(db); // Create the table if it doesn't exist
            }
        }
    }

    // Insert a new task into the database
    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask()); // Add task description to ContentValues
        cv.put(STATUS, 0); // Default status is 0 (not completed)
        db.insert(TODO_TABLE, null, cv); // Insert the ContentValues into the database
    }

    // Retrieve all tasks from the database
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID))); // Get task ID
                        task.setTask(cur.getString(cur.getColumnIndex(TASK))); // Get task description
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS))); // Get task status
                        taskList.add(task); // Add task to the list
                    } while (cur.moveToNext());
                }
            }
            db.setTransactionSuccessful(); // Mark the transaction as successful
        } finally {
            db.endTransaction(); // End the transaction
            if (cur != null) {
                cur.close(); // Close the cursor
            }
        }
        return taskList; // Return the list of tasks
    }

    // Update the status of a task in the database
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status); // Add new status to ContentValues
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)}); // Update the task status in the database
    }

    // Update the task description in the database
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task); // Add new task description to ContentValues
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)}); // Update the task description in the database
    }

    // Delete a task from the database
    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)}); // Delete the task from the database
    }
}

