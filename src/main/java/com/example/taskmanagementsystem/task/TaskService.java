package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.user.User;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(Long taskId, Task taskDetails);
    Task getTaskById(Long taskId);
    void deleteTask(Long taskId);
    Task updateTaskStatus(Long taskId, TaskStatus status);
    Task assignTask(Long taskId, User assignee);
    List<Task> getAllTasks();
}
