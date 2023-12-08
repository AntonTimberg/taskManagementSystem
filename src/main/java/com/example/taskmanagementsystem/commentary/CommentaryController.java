package com.example.taskmanagementsystem.commentary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentaryController {
    @Autowired
    private CommentaryService commentService;
    @Autowired
    private CommentaryConverter commentConverter;

    @PostMapping
    public CommentaryDto createComment(@PathVariable Long taskId, @RequestBody CommentaryDto commentDto) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID must not be null");
        }
        commentDto.setTaskId(taskId);
        Commentary comment = commentConverter.convert(commentDto);
        Commentary createdComment = commentService.createComment(taskId, comment);
        return commentConverter.convert(createdComment);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{commentId}")
    public CommentaryDto updateComment(@PathVariable Long commentId, @RequestBody String newContent) {
        Commentary updatedComment = commentService.updateComment(commentId, newContent);
        return commentConverter.convert(updatedComment);
    }
}
