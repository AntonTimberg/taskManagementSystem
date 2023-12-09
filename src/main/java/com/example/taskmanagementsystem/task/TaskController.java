package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.commentary.Commentary;
import com.example.taskmanagementsystem.commentary.CommentaryConverter;
import com.example.taskmanagementsystem.commentary.CommentaryDto;
import com.example.taskmanagementsystem.user.User;
import com.example.taskmanagementsystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private TaskConverter taskConverter;
    @Autowired
    private CommentaryConverter commentConverter;

    @GetMapping("/getAll")
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks().stream()
                .map(taskConverter::convert)
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        Task task = taskConverter.convert(taskDto);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String userEmail = ((UserDetails) principal).getUsername();
            User author = userService.getUserByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
            task.setAuthor(author);
        } else {
            task.setAuthor(null);
        }

        return taskConverter.convert(taskService.createTask(task));
    }



    @GetMapping("/get/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return taskConverter.convert(task);
    }

    @PutMapping("/update/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task updatedTask = taskService.updateTask(id, taskConverter.convert(taskDto));
        return taskConverter.convert(updatedTask);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/status/{id}")
    public TaskDto updateTaskStatus(@PathVariable Long id, @RequestBody String status) {
        Task task = taskService.updateTaskStatus(id, TaskStatus.valueOf(status.toUpperCase()));
        return taskConverter.convert(task);
    }

    @PatchMapping("/assignee/{id}")
    public TaskDto assignTask(@PathVariable Long id, @RequestBody String assigneeEmail) {
        User assignee = userService.getUserByEmail(assigneeEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + assigneeEmail));
        Task task = taskService.assignTask(id, assignee);
        return taskConverter.convert(task);
    }

    @GetMapping("/{taskId}/getComments")
    public Page<CommentaryDto> getCommentsForTask(@PathVariable Long taskId, Pageable pageable) {
        Page<Commentary> commentPage = taskService.getCommentsForTask(taskId, pageable);
        return commentPage.map(commentConverter::convert);
    }
}
