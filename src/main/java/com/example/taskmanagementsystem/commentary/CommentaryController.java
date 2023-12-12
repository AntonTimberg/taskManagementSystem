package com.example.taskmanagementsystem.commentary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentaryController {
    @Autowired
    private CommentaryService commentService;
    @Autowired
    private CommentaryConverter commentConverter;

    @PostMapping("/{taskId}")
    @Operation(summary = "Создать комментарий", description = "Создает новый комментарий для задачи")
    @ApiResponse(responseCode = "200", description = "Комментарий успешно создан",
            content = @Content(schema = @Schema(implementation = CommentaryDto.class)))
    public CommentaryDto createComment(@PathVariable Long taskId, @RequestBody CommentaryDto commentDto) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID must not be null");
        }
        Commentary comment = commentConverter.convert(commentDto);
        Commentary createdComment = commentService.createComment(taskId, comment);
        return commentConverter.convert(createdComment);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Удалить комментарий", description = "Удаляет комментарий по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Комментарий успешно удален")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        String currentUserEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        commentService.deleteComment(commentId, currentUserEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Обновить комментарий", description = "Обновляет содержимое комментария по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен",
            content = @Content(schema = @Schema(implementation = CommentaryDto.class)))
    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    @ApiResponse(responseCode = "400", description = "Неверный запрос или данные комментария")
    public CommentaryDto updateComment(@PathVariable Long commentId, @RequestBody CommentaryDto commentaryDto) {
        String currentUserEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Commentary updatedComment = commentService.updateComment(commentId, commentaryDto, currentUserEmail);
        return commentConverter.convert(updatedComment);
    }
}
