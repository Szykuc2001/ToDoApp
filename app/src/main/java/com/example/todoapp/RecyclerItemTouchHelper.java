package com.example.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.ToDoAdapter;

// Class to handle swipe actions on RecyclerView items
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter; // Adapter for managing tasks

    // Constructor for initializing the item touch helper with swipe directions
    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); // Enable left and right swipe
        this.adapter = adapter; // Set the adapter
    }

    // Method for handling drag and drop, not needed here so it returns false
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false; // Do not support moving items
    }

    // Method for handling swipe actions
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int position = viewHolder.getAdapterPosition(); // Get the position of the swiped item
        if(direction == ItemTouchHelper.LEFT){
            // If swiped left, show a confirmation dialog for deletion
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position); // Delete the item if confirmed
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition()); // Restore the item if cancelled
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show(); // Show the dialog
        }
        else {
            // If swiped right, edit the item
            adapter.editItem(position);
        }
    }

    // Method for drawing swipe background and icon
    @Override
    public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon; // Icon to be displayed
        ColorDrawable background; // Background color

        View itemView = viewHolder.itemView; // Get the item view
        int backgroundCornerOffset = 20; // Offset for background corners

        // Determine the swipe direction and set icon and background accordingly
        if(dX > 0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_edit); // Edit icon for right swipe
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark)); // Dark color for edit background
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete); // Delete icon for left swipe
            background = new ColorDrawable(Color.RED); // Red color for delete background
        }

        // Calculate icon's position
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        // Set icon bounds and background bounds based on swipe direction
        if(dX > 0){
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset , itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0,0,0,0); // No background if no swipe
        }
        background.draw(c); // Draw the background
        icon.draw(c); // Draw the icon
    }
}

