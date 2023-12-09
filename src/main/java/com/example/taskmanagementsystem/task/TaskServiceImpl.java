package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.commentary.Commentary;
import com.example.taskmanagementsystem.commentary.CommentaryRepo;
import com.example.taskmanagementsystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepo taskRepository;
    @Autowired
    private CommentaryRepo commentaryRepo;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long taskId, Task taskDetails) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));

        if (taskDetails.getTitle() != null) {
            task.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            task.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getStatus() != null) {
            validateTaskStatus(taskDetails.getStatus().name());
            task.setStatus(taskDetails.getStatus());
        }
        if (taskDetails.getPriority() != null) {
            validateTaskPriority(taskDetails.getPriority().name());
            task.setPriority(taskDetails.getPriority());
        }
        if (taskDetails.getAssignee() != null) {
            task.setAssignee(taskDetails.getAssignee());
        }

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
        taskRepository.delete(task);
    }

    @Override
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    public Task assignTask(Long taskId, User assignee) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Page<Commentary> getCommentsForTask(Long taskId, Pageable pageable) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
        return commentaryRepo.findByTaskId(taskId, pageable);
    }

    private void validateTaskStatus(String status) {
        try {
            TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid task status: " + status);
        }
    }

    private void validateTaskPriority(String priority) {
        try {
            TaskPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid task priority: " + priority);
        }
    }

    @Override
    public List<Task> getTasksByAuthor(String authorEmail) {
        return taskRepository.findByAuthorEmail(authorEmail);
    }

    @Override
    public List<Task> getTasksByAssignee(String assigneeEmail) {
        return taskRepository.findByAssigneeEmail(assigneeEmail);
    }
}
