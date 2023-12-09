package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class TaskConverter {

    @Autowired
    private UserService userService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm");


    public TaskDto convert(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setPriority(task.getPriority().name());
        dto.setAssigneeEmail(task.getAssignee() != null ? task.getAssignee().getEmail() : null);
        dto.setAuthorEmail(task.getAuthor() != null ? task.getAuthor().getEmail() : null);

        if (task.getCreatedTime() != null) {
            dto.setCreatedTime(task.getCreatedTime().format(formatter));
        }

        return dto;
    }

    public Task convert(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        if (dto.getAuthorEmail() != null) {
            task.setAuthor(userService.getUserByEmail(dto.getAuthorEmail()).orElse(null));
        }
        if (dto.getAssigneeEmail() != null) {
            task.setAssignee(userService.getUserByEmail(dto.getAssigneeEmail()).orElse(null));
        }
        if (dto.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(dto.getStatus()));
        } else {
            task.setStatus(TaskStatus.PENDING);
        }

        if (dto.getPriority() != null) {
            task.setPriority(TaskPriority.valueOf(dto.getPriority()));
        } else {
            task.setPriority(TaskPriority.LOW);
        }
        return task;
    }
}

