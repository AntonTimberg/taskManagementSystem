package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.commentary.Commentary;
import com.example.taskmanagementsystem.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskService {
    Task createTask(Task task);

    Task updateTask(Long taskId, Task taskDetails);

    Task getTaskById(Long taskId);

    void deleteTask(Long taskId);

    Task updateTaskStatus(Long taskId, TaskStatus status);

    Task assignTask(Long taskId, User assignee);

    Page<Task> findTasks(String filter, Pageable pageable);

    Page<Commentary> getCommentsForTask(Long taskId, Pageable pageable);

    Page<Task> getTasksByAuthor(String authorEmail, Pageable pageable);
    Page<Task> getTasksByAssignee(String assigneeEmail, Pageable pageable);
}
