package com.example.taskmanagementsystem.commentary;

import com.example.taskmanagementsystem.task.Task;
import com.example.taskmanagementsystem.task.TaskRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;


@Service
public class CommentaryServiceImpl implements CommentaryService {
    @Autowired
    private CommentaryRepo commentRepo;

    @Autowired
    private TaskRepo taskRepository;

    @Override
    public Commentary createComment(Long taskId, Commentary comment) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        comment.setTask(task);
        return commentRepo.save(comment);
    }

    public void deleteComment(Long commentId, String currentUserEmail) {
        Commentary comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!currentUserEmail.equals(comment.getAuthor().getEmail()) && !currentUserEmail.equals(comment.getTask().getAuthor().getEmail())) {
            try {
                throw new AccessDeniedException("Only author of the comment or the task can delete the comment");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        commentRepo.delete(comment);
    }

    public Commentary updateComment(Long commentId, CommentaryDto commentDto, String currentUserEmail) {
        Commentary existingComment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!currentUserEmail.equals(existingComment.getAuthor().getEmail())) {
            try {
                throw new AccessDeniedException("Only author of the comment can update it");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        existingComment.setContent(commentDto.getContent());
        return commentRepo.save(existingComment);
    }
}
