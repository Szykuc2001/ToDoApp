// StatsActivity.java

package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Utils.DatabaseHandler;

public class StatsActivity extends AppCompatActivity {

    private TextView totalTasksTextView;
    private TextView completedTasksTextView;
    private TextView pendingTasksTextView;
    private DatabaseHandler db;
    private Button backButton;

    // Method to set a custom DatabaseHandler for testing purposes
    public void setDatabaseHandler(DatabaseHandler dbHandler) {
        this.db = dbHandler;
        updateStats(); // Update stats with the injected db handler
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        totalTasksTextView = findViewById(R.id.totalTasksTextView);
        completedTasksTextView = findViewById(R.id.completedTasksTextView);
        pendingTasksTextView = findViewById(R.id.pendingTasksTextView);
        backButton = findViewById(R.id.backButton);

        if (db == null) {
            db = new DatabaseHandler(this);
            db.openDatabase();
        }

        updateStats();

        backButton.setOnClickListener(v -> finish());
    }

    private void updateStats() {
        if (db != null) {
            int totalTasks = db.getTasksCount();
            int completedTasks = db.getCompletedTasksCount();
            int pendingTasks = totalTasks - completedTasks;

            totalTasksTextView.setText("Total Tasks: " + totalTasks);
            completedTasksTextView.setText("Completed Tasks: " + completedTasks);
            pendingTasksTextView.setText("Pending Tasks: " + pendingTasks);
        }
    }

    public static void newInstance(Context context, Class<StatsActivity> statsActivityClass) {
        Intent intent = new Intent(context, statsActivityClass);
        context.startActivity(intent);
    }
}
