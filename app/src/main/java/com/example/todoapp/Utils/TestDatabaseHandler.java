package com.example.todoapp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

// Class for handling an in-memory test database by extending DatabaseHandler
public class TestDatabaseHandler extends DatabaseHandler {

    // Constructor for creating the in-memory test database handler
    public TestDatabaseHandler(Context context) {
        // Call the parent constructor with an in-memory database
        super(context, SQLiteDatabase.create(null));

        // Get a writable instance of the in-memory database
        SQLiteDatabase db = getWritableDatabase();

        // Check if the "todo" table exists in the in-memory database
        if (!isTableExists(db, "todo")) {
            // Create the "todo" table if it does not exist
            onCreate(db);
        }
    }
}
