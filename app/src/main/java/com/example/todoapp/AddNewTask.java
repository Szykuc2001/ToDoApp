package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// Class for adding a new task, displayed as a bottom sheet dialog
public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText; // EditText for entering the new task
    private Button newTaskSaveButton; // Button for saving the new task
    private DatabaseHandler db; // Database handler instance

    // Method to create a new instance of AddNewTask
    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    // Called when the fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle); // Set the style for the dialog
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false); // Inflate the layout

        // Adjust the soft input mode if the dialog's window is available
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        return view;
    }

    // Called when the view is created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText); // Initialize the EditText
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton); // Initialize the save button

        // Initialize the database handler if not already initialized
        if (db == null) {
            db = new DatabaseHandler(getActivity());
            db.openDatabase();
        }

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        // Check if we are updating an existing task
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task); // Set the task text in the EditText
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); // Change button color if there's text
        }

        // Add a text watcher to enable/disable the save button based on text input
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false); // Disable save button if text is empty
                    newTaskSaveButton.setTextColor(Color.GRAY); // Set button color to gray
                } else {
                    newTaskSaveButton.setEnabled(true); // Enable save button if text is not empty
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); // Set button color to primary dark
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });

        boolean finalIsUpdate = isUpdate;
        // Set a click listener for the save button
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString(); // Get the text from the EditText
                if (finalIsUpdate) {
                    db.updateTask(bundle.getInt("id"), text); // Update the task if it's an existing task
                } else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text); // Set the task text
                    task.setStatus(0); // Set the status to not completed
                    db.insertTask(task); // Insert the new task into the database
                }
                dismiss(); // Dismiss the dialog
            }
        });
    }

    // Called when the dialog is dismissed
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        // Notify the activity that the dialog has been dismissed
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }

    // Setter method for the database handler
    public void setDatabaseHandler(DatabaseHandler db) {
        this.db = db;
    }
}

