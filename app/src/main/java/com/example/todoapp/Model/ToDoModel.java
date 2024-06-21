package com.example.todoapp.Model;

// Model class representing a ToDo item
public class ToDoModel {
    private int id; // Unique identifier for the task
    private int status; // Status of the task (e.g., completed or not completed)
    private String task; // Description of the task

    // Getter method for the task ID
    public int getId() {
        return id;
    }

    // Setter method for the task ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter method for the task status
    public int getStatus() {
        return status;
    }

    // Setter method for the task status
    public void setStatus(int status) {
        this.status = status;
    }

    // Getter method for the task description
    public String getTask() {
        return task;
    }

    // Setter method for the task description
    public void setTask(String task) {
        this.task = task;
    }
}
