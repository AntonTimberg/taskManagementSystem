package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.user.User;
import com.example.taskmanagementsystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    private TaskConverter taskConverter;

    @GetMapping("/getAll")
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks().stream()
                .map(taskConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        Task task = taskConverter.fromDto(taskDto);
        return taskConverter.toDto(taskService.createTask(task));
    }

    @GetMapping("/get/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return taskConverter.toDto(task);
    }

    @PutMapping("/update/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task updatedTask = taskService.updateTask(id, taskConverter.fromDto(taskDto));
        return taskConverter.toDto(updatedTask);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/status/{id}")
    public TaskDto updateTaskStatus(@PathVariable Long id, @RequestBody String status) {
        Task task = taskService.updateTaskStatus(id, TaskStatus.valueOf(status.toUpperCase()));
        return taskConverter.toDto(task);
    }

    @PatchMapping("/assignee/{id}")
    public TaskDto assignTask(@PathVariable Long id, @RequestBody String assigneeEmail) {
        User assignee = userService.getUserByEmail(assigneeEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + assigneeEmail));
        Task task = taskService.assignTask(id, assignee);
        return taskConverter.toDto(task);
    }
}
