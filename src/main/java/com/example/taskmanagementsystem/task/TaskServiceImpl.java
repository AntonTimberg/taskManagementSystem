package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.commentary.Commentary;
import com.example.taskmanagementsystem.commentary.CommentaryRepo;
import com.example.taskmanagementsystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;


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

        String currentUserEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        if (!currentUserEmail.equals(task.getAuthor().getEmail())) {
            try {
                throw new AccessDeniedException("Only author can update the task");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

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

        String currentUserEmail = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (!currentUserEmail.equals(task.getAuthor().getEmail())) {
            try {
                throw new AccessDeniedException("Only author can delete the task");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

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
    public Page<Task> findTasks(String filter, Pageable pageable) {
        if (filter == null || filter.isEmpty()) {
            return taskRepository.findAll(pageable);
        } else {
            return taskRepository.findByTitleContaining(filter, pageable);
        }
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
    public Page<Task> getTasksByAuthor(String authorEmail, Pageable pageable) {
        return taskRepository.findByAuthorEmail(authorEmail, pageable);
    }
    @Override
    public Page<Task> getTasksByAssignee(String assigneeEmail, Pageable pageable) {
        return taskRepository.findByAssigneeEmail(assigneeEmail, pageable);
    }
}
