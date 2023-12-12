package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.commentary.Commentary;
import com.example.taskmanagementsystem.commentary.CommentaryConverter;
import com.example.taskmanagementsystem.commentary.CommentaryDto;
import com.example.taskmanagementsystem.user.User;
import com.example.taskmanagementsystem.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.Map;


@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "api для работы с задачами")
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
    @Operation(summary = "Получить все задачи", description = "Возвращает список всех задач")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка задач",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class))))
    public Page<TaskDto> getTasks(@RequestParam(required = false) String filter, Pageable pageable) {
        Page<Task> tasks = taskService.findTasks(filter, pageable);
        return tasks.map(taskConverter::convert);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание задачи", description = "Создает новую задачу с данными, предоставленными в теле запроса")
    @ApiResponse(responseCode = "201", description = "Задача успешно создана",
            content = @Content(schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "400", description = "Неверные данные задачи")
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

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по указанному ID")
    @ApiResponse(responseCode = "200", description = "Задача найдена",
            content = @Content(schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public TaskDto getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return taskConverter.convert(task);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить задачу", description = "Обновляет задачу с указанным ID данными из тела запроса")
    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена",
            content = @Content(schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "400", description = "Неверные данные для обновления задачи")
    @ApiResponse(responseCode = "404", description = "Задача для обновления не найдена")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task updatedTask = taskService.updateTask(id, taskConverter.convert(taskDto));
        return taskConverter.convert(updatedTask);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по указанному ID")
    @ApiResponse(responseCode = "200", description = "Задача успешно удалена")
    @ApiResponse(responseCode = "404", description = "Задача для удаления не найдена")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/status/{id}")
    @Operation(summary = "Обновить статус задачи", description = "Обновляет статус задачи с указанным ID")
    @ApiResponse(responseCode = "200", description = "Статус задачи успешно обновлен",
            content = @Content(schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "400", description = "Неверный статус задачи")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public TaskDto updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        Task task = taskService.getTaskById(id);

        String currentUserEmail = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        if (!currentUserEmail.equals(task.getAuthor().getEmail()) && !currentUserEmail.equals(task.getAssignee().getEmail())) {
            try {
                throw new AccessDeniedException("Only author or assignee can update task status");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        return taskConverter.convert(taskService.updateTaskStatus(id, TaskStatus.valueOf(status.toUpperCase())));
    }

    @PatchMapping("/assignee/{id}")
    @Operation(summary = "Назначить задачу пользователю", description = "Назначает задачу с указанным ID пользователю с заданным email")
    @ApiResponse(responseCode = "200", description = "Задача успешно назначена пользователю",
            content = @Content(schema = @Schema(implementation = TaskDto.class)))
    @ApiResponse(responseCode = "404", description = "Задача или пользователь не найдены")
    public TaskDto assignTask(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String assigneeEmail = body.get("email");
        User assignee = userService.getUserByEmail(assigneeEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + assigneeEmail));
        Task task = taskService.assignTask(id, assignee);
        return taskConverter.convert(task);
    }

    @GetMapping("/{taskId}/getComments")
    @Operation(summary = "Получить комментарии задачи", description = "Возвращает список комментариев для задачи с указанным ID")
    @ApiResponse(responseCode = "200", description = "Комментарии успешно получены",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentaryDto.class))))
    public Page<CommentaryDto> getCommentsForTask(@PathVariable Long taskId, Pageable pageable) {
        Page<Commentary> commentPage = taskService.getCommentsForTask(taskId, pageable);
        return commentPage.map(commentConverter::convert);
    }

    @GetMapping("/byAuthor/{authorEmail}")
    @Operation(summary = "Получить задачи по автору", description = "Возвращает список задач, созданных пользователем с указанным email")
    @ApiResponse(responseCode = "200", description = "Задачи автора успешно получены",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class))))
    @ApiResponse(responseCode = "404", description = "Автор не найден")
    public Page<TaskDto> getTasksByAuthor(@PathVariable String authorEmail, Pageable pageable) {
        if (!userService.existsByEmail(authorEmail)) {
            throw new RuntimeException("Author not found with email: " + authorEmail);
        }
        Page<Task> taskPage = taskService.getTasksByAuthor(authorEmail, pageable);
        return taskPage.map(taskConverter::convert);
    }

    @GetMapping("/byAssignee/{assigneeEmail}")
    @Operation(summary = "Получить задачи по исполнителю", description = "Возвращает список задач, назначенных пользователю с указанным email")
    @ApiResponse(responseCode = "200", description = "Задачи исполнителя успешно получены",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class))))
    @ApiResponse(responseCode = "404", description = "Исполнитель не найден")
    public Page<TaskDto> getTasksByAssignee(@PathVariable String assigneeEmail, Pageable pageable) {
        if (!userService.existsByEmail(assigneeEmail)) {
            throw new RuntimeException("Assignee not found with email: " + assigneeEmail);
        }
        Page<Task> taskPage = taskService.getTasksByAssignee(assigneeEmail, pageable);
        return taskPage.map(taskConverter::convert);
    }
}
