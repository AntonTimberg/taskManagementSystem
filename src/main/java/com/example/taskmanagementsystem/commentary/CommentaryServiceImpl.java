package com.example.taskmanagementsystem.commentary;

import com.example.taskmanagementsystem.task.Task;
import com.example.taskmanagementsystem.task.TaskRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public void deleteComment(Long commentId) {
        Commentary comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
        commentRepo.delete(comment);
    }

    public Commentary updateComment(Long commentId, CommentaryDto commentDto) {
        Commentary existingComment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        existingComment.setContent(commentDto.getContent());
        return commentRepo.save(existingComment);
    }
}
