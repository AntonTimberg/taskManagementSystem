package com.example.taskmanagementsystem.commentary;

import com.example.taskmanagementsystem.task.Task;
import com.example.taskmanagementsystem.task.TaskRepo;
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

    @Override
    public void deleteComment(Long commentId) {
        Commentary comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        commentRepo.delete(comment);
    }

    @Override
    public Commentary updateComment(Long commentId, String newContent) {
        Commentary comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        comment.setContent(newContent);
        return commentRepo.save(comment);
    }
}
