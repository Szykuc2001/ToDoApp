package com.example.todoapp;

import android.content.DialogInterface;

// Interface for handling dialog close events
public interface DialogCloseListener {

    // Method to handle the dialog close event
    public void handleDialogClose(DialogInterface dialog);
}
